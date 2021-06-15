package com.tmtravlr.additions.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.InvalidBlockStateException;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Holds info about a block and that state it should be in.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @since July 2019
 */
public class BlockStateInfo {
	
	private IBlockState blockState;
	private Predicate<IBlockState> predicate;
	
	private Block block;
	private Map<String, String> stateMap;
	
	public BlockStateInfo(Block block) {
		this(block, Collections.EMPTY_MAP);
	}
	
	public BlockStateInfo(Block block, Map<String, String> stateMap) {
		this.block = block;
		this.stateMap = stateMap;
		String stateString = "";
		
		if (!this.stateMap.isEmpty()) {
			StringJoiner stateStringJoiner = new StringJoiner(",");
			this.stateMap.forEach((key, value) -> stateStringJoiner.add(key + "=" + value));
			stateString = stateStringJoiner.toString();
		}
				
		try {
			if (stateString.isEmpty()) {
				this.predicate = state -> state.getBlock() == this.block;
			} else {
				this.predicate = CommandBase.convertArgToBlockStatePredicate(block, stateString);
			}
		} catch (InvalidBlockStateException e) {
			AdditionsMod.logger.error("Unable to parse block state predicate. Setting to never. Message is: '" + I18n.translateToLocalFormatted(e.getMessage(), e.getErrorObjects()) + "', and state is: '" + stateString + "'");
			this.predicate = Predicates.alwaysFalse();
		}
		
		try {
			this.blockState = CommandBase.convertArgToBlockState(block, stateString.isEmpty() ? "default" : stateString);
		} catch (InvalidBlockStateException | NumberInvalidException e) {
			AdditionsMod.logger.error("Unable to parse block state. Setting to default. Message is: '" + I18n.translateToLocalFormatted(e.getMessage(), e.getErrorObjects()) + "', and state is: '" + stateString + "'");
			this.blockState = block.getDefaultState();
		}
	}
	
	public BlockStateInfo(IBlockState blockState) {
		this.block = blockState.getBlock();
		this.blockState = blockState;
		String stateString = "";
		
		if (!blockState.getProperties().isEmpty()) {
			this.stateMap = this.blockState.getProperties().entrySet().stream().collect(Collectors.toMap(Object::toString, Object::toString));
			
			StringJoiner stateStringJoiner = new StringJoiner(",");
			this.stateMap.forEach((key, value) -> stateStringJoiner.add(key + "=" + value));
			stateString = stateStringJoiner.toString();
		}
			
		try {
			if (stateString.isEmpty()) {
				this.predicate = state -> state.getBlock() == this.block;
			} else {
				this.predicate = CommandBase.convertArgToBlockStatePredicate(block, stateString);
			}
		} catch (InvalidBlockStateException e) {
			AdditionsMod.logger.error("Unable to parse block state predicate. Setting to never. Message is: '" + I18n.translateToLocalFormatted(e.getMessage(), e.getErrorObjects()) + "', and state is: '" + stateString + "'");
			this.predicate = Predicates.alwaysFalse();
		}
	}
	
	public boolean matches(IBlockState input) {
		return this.predicate.test(input);
	}
	
	public IBlockState getBlockState() {
		return this.blockState;
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	public Map<String, String> getStateMap() {
		return this.stateMap;
	}
	
	public static void testPropertyMap(Block block, Map<String, String> propertyMap) throws InvalidBlockStateException {
		StringJoiner stateString = new StringJoiner(",");
		propertyMap.forEach((key, value) -> stateString.add(key + "=" + value));
		CommandBase.convertArgToBlockStatePredicate(block, stateString.toString());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
		result = prime * result + ((blockState == null) ? 0 : blockState.hashCode());
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result + ((stateMap == null) ? 0 : stateMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockStateInfo other = (BlockStateInfo) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (blockState == null) {
			if (other.blockState != null)
				return false;
		} else if (!blockState.equals(other.blockState))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		if (stateMap == null) {
			if (other.stateMap != null)
				return false;
		} else if (!stateMap.equals(other.stateMap))
			return false;
		return true;
	}
	
	public ItemStack getDisplayStack() {
		ItemStack displayStack = ItemStack.EMPTY;
		
		if (this.block != null) {
			try {
				displayStack = this.block.getPickBlock(this.blockState, null, null, null, null);
			} catch (Throwable e) {
				//Do nothing, just leave the item stack empty
			}
		}
		
		return displayStack;
	}
	
	public static class Serializer {
		
		public static JsonElement serialize(BlockStateInfo info) {
			JsonObject json = new JsonObject();
			
			if (info.block == null) {
				throw new IllegalArgumentException("Can't serialize block state info without a block!");
			}
			
			json.addProperty("block", info.block.getRegistryName().toString());
			
			if (!info.stateMap.isEmpty()) {
				JsonObject jsonState = new JsonObject();
				info.stateMap.forEach((key, value) -> jsonState.addProperty(key, value));
				json.add("state", jsonState);
			}
			
			return json;
		}
		
		public static BlockStateInfo deserialize(JsonObject json) {
			String blockName = JsonUtils.getString(json, "block");
			Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
			
			if (block == null) {
				throw new JsonParseException("Expected to find a block, found unknown string " + blockName);
			}
			
			Map<String, String> stateMap = new HashMap<>();
			
			if (json.has("state")) {
				JsonObject jsonState = JsonUtils.getJsonObject(json, "state");
				
				jsonState.entrySet().forEach(entry -> {
					if (entry.getValue().isJsonPrimitive() && entry.getValue().getAsJsonPrimitive().isString()) {
						stateMap.put(entry.getKey(), entry.getValue().getAsJsonPrimitive().getAsString());
					} else {
						throw new JsonParseException("Expected to find a string value for '" + entry.getKey() + "', found " + entry.getValue());
					}
				});
			}
			
			return new BlockStateInfo(block, stateMap);
		}
		
	}

}

package com.tmtravlr.additions.gui.view.components.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.tmtravlr.additions.gui.view.components.IGuiViewComponent;
import com.tmtravlr.additions.gui.view.components.helpers.GuiScrollingComponentList;
import com.tmtravlr.additions.gui.view.edit.GuiEdit;

/**
 * Displays a list of other components. Useful for lists of things like item lore.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date August 2017
 */
public abstract class GuiComponentListInput<T extends IGuiViewComponent> implements IGuiViewComponent {
	
    private static final int ADD_COMPONENT_HEIGHT = 15;
    private static final int INSERT_COMPONENT_HEIGHT = 6;
	
	public GuiEdit editScreen;
	public IGuiViewComponent addNewComponent = new GuiComponentAddSymbol();
	public IGuiViewComponent insertNewComponent = new GuiComponentInsertSymbol();

	private ArrayList<T> components = new ArrayList<>();
	private GuiScrollingComponentList<T> componentList;
	private String label;
	private int x;
	private int y;
	private int width;
	private boolean hidden = false;
	private boolean required = false;
	
	public GuiComponentListInput(String label, GuiEdit editScreen) {
		this.editScreen = editScreen;
		this.label = label;
		this.recreateComponentList();
	}

	@Override
	public boolean isHidden() {
		return this.hidden;
	}
	
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public void setRequired() {
		this.required = true;
	}

	@Override
	public int getHeight(int left, int right) {
		return Math.min(this.componentList.getListHeight() + 5, 180) + 20;
	}
	
	@Override
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public boolean isRequired() {
		return this.required;
	}

	@Override
	public void drawInList(int x, int y, int right, int mouseX, int mouseY) {
		this.x = x;
		this.y = y + 10;
		this.width = right - x - 10;
		
		this.componentList.setNewDimensions(this.x, this.y, this.width, this.getHeight(this.x, this.x + this.width) - 20);
		this.componentList.drawScreen(mouseX, mouseY, 0);
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.componentList.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {
		if (this.isHovering(mouseX, mouseY) && this.componentList.getListHeight() + 25 > this.getHeight(this.x, this.x + this.width)) {
			this.componentList.handleMouseInput(mouseX, mouseY);
			return true;
		}
		return false;
	}

	@Override
	public void onKeyTyped(char keyTyped, int keyCode) throws IOException {
		this.componentList.onKeyTyped(keyTyped, keyCode);
	}
	
	@Override
	public void onInitGui() {
		
	}
	
	private boolean isHovering(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.getHeight(this.x, this.x + this.width);
	}
	
	private void recreateComponentList() {
		
		float scrollDistance = 0;
		if (this.componentList != null) {
			scrollDistance = this.componentList.getScrollDistance();
		}
		
		this.componentList = new GuiScrollingComponentList<T>(this, 0, 0, 0, 0);
		this.componentList.setScrollDistance(scrollDistance);
		
		for (T component : this.components) {
			this.componentList.addComponent(this.insertNewComponent);
			this.componentList.addComponent(component);
		}
		this.componentList.addComponent(this.addNewComponent);
	}
	
	public void addNewComponent() {
		T blankComponent = this.createBlankComponent();
		this.components.add(blankComponent);
		this.recreateComponentList();
		this.componentList.setScrollDistance(this.componentList.getScrollDistance() + blankComponent.getHeight(this.x, this.x + this.width));
		this.editScreen.notifyHasChanges();
	}
	
	public void insertNewComponent(int index) {
		if (index >= 0 && index < this.components.size()) {
			T blankComponent = this.createBlankComponent();
			this.components.add(index, blankComponent);
			this.recreateComponentList();
			this.componentList.setScrollDistance(this.componentList.getScrollDistance() + blankComponent.getHeight(this.x, this.x + this.width));
			this.editScreen.notifyHasChanges();
		}
	}
	
	public void moveComponentUp(int index) {
		if (index >= 1 && index < this.components.size()) {
			Collections.swap(this.components, index, index - 1);
			this.recreateComponentList();
			this.editScreen.notifyHasChanges();
		}
	}
	
	public void moveComponentDown(int index) {
		if (index >= 0 && index < this.components.size() - 1) {
			Collections.swap(this.components, index, index + 1);
			this.recreateComponentList();
			this.editScreen.notifyHasChanges();
		}
	}
	
	public void removeComponent(int index) {
		if (index >= 0 && index < this.components.size()) {
			this.components.remove(index);
			this.recreateComponentList();
			this.editScreen.notifyHasChanges();
		}
	}
	
	public void removeAllComponents() {
		this.components.clear();
		this.recreateComponentList();
		this.editScreen.notifyHasChanges();
	}
	
	public List<T> getComponents() {
		return this.components;
	}
	
	public void setDefaultComponents(Collection<T> toAdd) {
		this.components = new ArrayList<>(toAdd);
		this.recreateComponentList();
	}
	
	public void addDefaultComponent(T toAdd) {
		this.components.add(toAdd);
		this.recreateComponentList();
	}

	public abstract T createBlankComponent();
	
	protected class GuiComponentAddSymbol implements IGuiViewComponent {
		
		private boolean hidden = false;

		@Override
		public boolean isHidden() {return this.hidden;}
		
		@Override
		public void setHidden(boolean hidden) {this.hidden = hidden;}
		
		@Override
		public int getHeight(int left, int right) {return ADD_COMPONENT_HEIGHT;}

		@Override
		public void drawInList(int x, int y, int right, int mouseX, int mouseY) {}

		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}

		@Override
		public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {return false;}

		@Override
		public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	}
	
	protected class GuiComponentInsertSymbol implements IGuiViewComponent {
		
		private boolean hidden = false;

		@Override
		public boolean isHidden() {return this.hidden;}
		
		@Override
		public void setHidden(boolean hidden) {this.hidden = hidden;}
		
		@Override
		public int getHeight(int left, int right) {return INSERT_COMPONENT_HEIGHT;}

		@Override
		public void drawInList(int x, int y, int right, int mouseX, int mouseY) {}

		@Override
		public void onMouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}

		@Override
		public boolean onHandleMouseInput(int mouseX, int mouseY) throws IOException {return false;}

		@Override
		public void onKeyTyped(char keyTyped, int keyCode) throws IOException {}
	}

}

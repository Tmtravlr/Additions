package com.tmtravlr.additions.gui.edit.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.additions.gui.edit.GuiEdit;
import com.tmtravlr.additions.gui.edit.IGuiEditComponent;
import com.tmtravlr.additions.gui.edit.components.helpers.GuiScrollingComponentList;

public abstract class GuiComponentList<T extends IGuiEditComponent> implements IGuiEditComponent {
	
    private static final int ADD_COMPONENT_HEIGHT = 40;
    private static final int INSERT_COMPONENT_HEIGHT = 6;
	
	public GuiEdit parent;
	public IGuiEditComponent addNewComponent = new IGuiEditComponent() {
		
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
	};
	public IGuiEditComponent insertNewComponent = new IGuiEditComponent() {
		
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
	};

	private ArrayList<T> components = new ArrayList<>();
	private GuiScrollingComponentList<T> componentList;
	private String label;
	private int x;
	private int y;
	private int width;
	
	public GuiComponentList(String label, GuiEdit parent) {
		this.parent = parent;
		this.label = label;
		this.recreateComponentList();
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
		this.components.add(this.createBlankComponent());
		this.recreateComponentList();
	}
	
	public void insertNewComponent(int index) {
		if (index >= 0 && index < this.components.size()) {
			this.components.add(index, this.createBlankComponent());
			this.recreateComponentList();
		}
	}
	
	public void removeComponent(int index) {
		if (index >= 0 && index < this.components.size()) {
			this.components.remove(index);
			this.recreateComponentList();
		}
	}
	
	public List<T> getComponents() {
		return this.components;
	}

	public abstract T createBlankComponent();

}

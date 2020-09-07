package com.tmtravlr.additions.gui.message;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.tmtravlr.additions.AdditionsMod;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Shows a message saying to choose a file, since otherwise the game just freezes and people might get confused.
 * 
 * @author Tmtravlr (Rebeca Rey)
 * @date September 2017
 */
public class GuiMessageBoxSelectFile extends GuiMessageBox {
	
	private final JFileChooser fileChooser;
	private final Consumer<Integer> callback;
	private Integer chooserState = null;

	public GuiMessageBoxSelectFile(GuiScreen parentScreen, JFileChooser fileChooser, Consumer<Integer> callback) {
		super(parentScreen, I18n.format("gui.popup.selectFile.title"), new TextComponentTranslation("gui.popup.selectFile.message"), "");
		this.fileChooser = fileChooser;
		this.callback = callback;
		
		Thread showChooserThread = new Thread(this::showFileChooser, "Show File Chooser");
		showChooserThread.start();
	}

	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.clear();
	}
	
	@Override
	public void updateScreen() {
		if (this.chooserState != null) {
			this.mc.displayGuiScreen(this.parentScreen);
			this.callback.accept(this.chooserState);
		}
	}
	
	private void showFileChooser() {
		try {
			EventQueue.invokeAndWait(() -> {
				JFrame frame = new JFrame( "Dialog" );
		        frame.setAlwaysOnTop( true );
		        this.chooserState = fileChooser.showOpenDialog( frame );
		        frame.dispose();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			AdditionsMod.logger.warn("There was a problem showing the file chooser.", e);
			this.chooserState = JFileChooser.CANCEL_OPTION;
		}
	}

}

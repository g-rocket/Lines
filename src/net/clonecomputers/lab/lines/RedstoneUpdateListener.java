package net.clonecomputers.lab.lines;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class RedstoneUpdateListener implements Listener {
	
	@SuppressWarnings("unused") private Lines plugin;
	
	public RedstoneUpdateListener(Lines plugin){
		this.plugin = plugin;
	}
	
	public void onRedstonePower(BlockRedstoneEvent e){
		Block b = e.getBlock();
		if(!b.getType().equals(Material.DETECTOR_RAIL)) return;
		if(!b.hasMetadata(Lines.railMetadataKey)) return;
		//e.setNewCurrent(plugin.shouldBeOn(e.getBlock()));
	}
}

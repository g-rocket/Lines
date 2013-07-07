package net.clonecomputers.lab.lines;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.material.*;

// by Gio Rescigno

public class MinecartMoveListener implements Listener {


	private final Lines plugin;

	public MinecartMoveListener(Lines plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onMinecartMoved(VehicleUpdateEvent e){
		if(!(e.getVehicle() instanceof Minecart)) return;
		Minecart m = (Minecart)e.getVehicle();
		if(m.hasMetadata(Lines.minecartActivatingRailKey)){
			Location minecartLoc = m.getLocation();
			int[] railPos = (int[])m.getMetadata(Lines.minecartActivatingRailKey).get(0).value();
			final Location railLoc = new Location(minecartLoc.getWorld(),railPos[0],railPos[1],railPos[2]);
			if(minecartLoc.getBlock().equals(railLoc.getBlock())){
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					@Override
					public void run() {
						plugin.setDetectorRail(railLoc.getBlock(), false);
					}
					
				}, 10);
			}
		}
		Block railBlock = m.getLocation().getBlock();
		if (!railBlock.getType().equals(Material.RAILS) &&
				!railBlock.getType().equals(Material.ACTIVATOR_RAIL)) return; //checks if the minecart is on a Rail
		if (!railBlock.hasMetadata(Lines.railMetadataKey)) return; //checks if the rail has the right metadata
		if (railBlock.getType().equals(Material.RAILS)){
			plugin.minecartOnDetector(m, railBlock);
		} else {
			plugin.minecartOnActivator(m, railBlock);
		}
	}
	
	@SuppressWarnings("unused") private Block[] adjacentRails(Block b){ //TODO: do better
		Rails r = new Rails(b.getType(),b.getData());
		return new Block[]{b,b.getRelative(r.getDirection()),b.getRelative(r.getDirection(), -1)};
	}
	
	
}

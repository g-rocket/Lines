package net.clonecomputers.lab.lines;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.java.*;

public class Lines extends JavaPlugin{
	
	public static String railMetadataKey = "lines.rail.allowedLines";
	public static String minecartMetadataKey = "lines.cart.line";
	public static String minecartActivatingRailKey = "lines.cart.activating";
	
	private LinesCommandExecutor commandExecutor = new LinesCommandExecutor(this);
	private MinecartMoveListener moveListener = new MinecartMoveListener(this);
	//private RedstoneUpdateListener redstoneListener = new RedstoneUpdateListener(this);
	
    public void onDisable() {
    	getCommand("lines").setExecutor(null);
		getLogger().info("Lines 1.0 is disabled!");
	}

	public void onEnable() {
		getLogger().info("Lines 1.0 is enabled!");
        getCommand("lines").setExecutor(commandExecutor);
        getServer().getPluginManager().registerEvents(moveListener, this);
        //getServer().getPluginManager().registerEvents(redstoneListener, this);
	}
	
	public void minecartOnDetector(Minecart minecart, Block railBlock){
		setDetectorRail(railBlock,detectorRailShouldBeOn(minecart,railBlock));
	}
	
	public boolean detectorRailShouldBeOn(Minecart minecart, Block railBlock){
		String[] lines = ((String[])railBlock.getMetadata(Lines.railMetadataKey).get(0).value());
		String lineOfMinecart = null;
		if(minecart.hasMetadata(Lines.minecartMetadataKey)){
			lineOfMinecart = ((String)minecart.getMetadata(Lines.minecartMetadataKey).get(0).value());
		}

		boolean shouldBePressed = false;

		if(lineOfMinecart != null) for(String line: lines){
			if(line == null) continue;
			if (line.equals(lineOfMinecart)){
				shouldBePressed = true;
			}
		}
		if(lines[0] == null) shouldBePressed = !shouldBePressed; // negate
		getServer().broadcastMessage("minecart is "+lineOfMinecart+", should be"+lines);
		return shouldBePressed;
	}
	
	public void setDetectorRail(Block railBlock, boolean shouldBePressed){
		Block b = railBlock.getRelative(BlockFace.DOWN);
		if(shouldBePressed){
			b.setType(Material.REDSTONE_BLOCK);
			//b.setData((byte)0);
		}else{
			b.setType(Material.WOOL);
			b.setData(DyeColor.RED.getWoolData());
		}
		/*DetectorRail rail = new DetectorRail(railBlock.getType(), railBlock.getData());
		rail.setPressed(shouldBePressed);
		railBlock.setData(rail.getData());*/
		getServer().broadcastMessage("setting to "+(shouldBePressed?"on":"off"));
	}
	
	public void minecartOnActivator(Minecart minecart, Block railBlock){
		String line = null;
		if((railBlock.getData() & 0x08) == 0){ //HACK: should be true if rail is unpowered
			line = ((String[])railBlock.getMetadata(Lines.railMetadataKey).get(0).value())[0];
		}else{
			line = ((String[])railBlock.getMetadata(Lines.railMetadataKey).get(0).value())[1];
		}
		if(line != null) minecart.setMetadata(Lines.minecartMetadataKey, new FixedMetadataValue(this, line));
		getServer().broadcastMessage("setting minecart to line "+line);
		
	}

	public boolean shouldBeOn(Block block) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
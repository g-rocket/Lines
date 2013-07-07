package net.clonecomputers.lab.lines;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;

public class LinesCommandExecutor implements CommandExecutor {

	private Lines plugin;
	
	public LinesCommandExecutor(Lines plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("/lines can only be called by a player");
			return false;
		}
		if(args.length < 1){
			sender.sendMessage("invalid argument number");
			return false;
		}
		Player p = (Player)sender;
		Block rail = p.getTargetBlock(null, 10);
		if(rail.getType().equals(Material.ACTIVATOR_RAIL)){
			if(args[0].trim().equalsIgnoreCase("on")){
				if(args.length < 2){
					sender.sendMessage("invalid argument number");
					return false;
				}
				String offLine = null;
				if(rail.hasMetadata(Lines.railMetadataKey)){
					offLine = ((String[])rail.getMetadata(Lines.railMetadataKey).get(0).value())[0];
				}
				rail.setMetadata(Lines.railMetadataKey, new FixedMetadataValue(plugin, new String[]{offLine,args[1]}));
			}else if(args[0].trim().equalsIgnoreCase("off")){
				if(args.length < 2){
					sender.sendMessage("invalid argument number");
					return false;
				}
				String onLine = null;
				if(rail.hasMetadata(Lines.railMetadataKey)){
					onLine = ((String[])rail.getMetadata(Lines.railMetadataKey).get(0).value())[1];
				}
				rail.setMetadata(Lines.railMetadataKey, new FixedMetadataValue(plugin, new String[]{onLine,args[0]}));
			}else{
				rail.setMetadata(Lines.railMetadataKey, new FixedMetadataValue(plugin, new String[]{args[0],args[0]}));
			}
		}else if(rail.getType().equals(Material.RAILS)){
			if(args[0].trim().equalsIgnoreCase("not")) args[0] = null;
			rail.setMetadata(Lines.railMetadataKey, new FixedMetadataValue(plugin, args));
		}
		return true;
	}
}

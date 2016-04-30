package com.haniokasai.nukkit.KillBearBoys;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;


/*
Logs
	13.8.18 1.0 Begin
	13.8.20 1.0.1
	13.8.24 1.0.2
	13.11.16 2.0
		-rewrite all things
	13.11.23 2.0.1
		-Fix bug
	14.3.1 2.0.2
		-Remove default timezone
    14.10.18 2.0.3 and 2.0.4
             -1.4Beta Support(ljy Thank you!)
	15.09.16 3.0.0
		-Database changed to sqlite3.(haniokasai)
	16.04.28 0.0.1n
		-for nukkit.
 -thanks hmy2001! http://hmy2001.dip.jp/Blog/?m=201510
*/


public class Main extends PluginBase implements Listener{

	Connection connection = null;

	Config config;
	public void onEnable() {

		 this.getServer().getPluginManager().registerEvents(this, this);
		getDataFolder().mkdir();
		@SuppressWarnings("deprecation")
		Config config = new Config(
	                new File(this.getDataFolder(), "config.yml"),Config.YAML,
	                new LinkedHashMap<String, Object>() {
	                    {
	                    	put("//settings", "///////");
	                    	put("itemid", 294);
	                    	put("metaid", 0);
	                    	put("/////////", "/////");
	                    	put("xyz", "base64");
	                    }
	                });

//		     statement.executeUpdate("CREATE TABLE IF NOT EXISTS  block (xyz TEXT PRIMARY KEY, level TEXT , who TEXT ,ip TEXT, cid TEXT, action TEXT, time TEXT, blockname TEXT, blockid INT, meta INT ,skinid TEXT)");





        config.save();
        this.getServer().getLogger().info("[AdminNotice] Loaded");

}

	public void onDisable() {
	}


	@EventHandler
	public void t(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(player.hasPermission("KillBearBoys.touch")){
			double x = event.getBlock().x;
			double y = event.getBlock().y;
			double z = event.getBlock().z;
			String xyz ="x"+x+"y"+y+"z"+z;
			if(config.exists(xyz)){

				String j13 =config.get(xyz).toString();
		        String decoded = new String(Base64.getDecoder()
		                .decode(j13));
		        JSONObject json = new JSONObject(decoded);
		        Iterator<String> keys = json.keys();
		        Map<String, String> map = new HashMap<String, String>();
		        while (keys.hasNext()) {
		            String key = keys.next().toString();
		            String val = json.getString(key);
		            map.put(key, val);
		        }
				player.sendMessage("[KillBearBoys]////////////////////");
				player.sendMessage("[XYZ] "+xyz);
				player.sendMessage("[Name] "+map.get("who"));
				player.sendMessage("[IP] "+map.get("ip")+" [CID] "+map.get("cid"));
				player.sendMessage("[Action]"+map.get("action"));
				player.sendMessage("[Block] "+map.get("blockname")+" [ID] "+map.get("blockid")+ ":"+map.get("meta"));
				player.sendMessage("[Date] "+map.get("time"));
				player.sendMessage("////////////////////////////////");

			}
		}
	}
	@EventHandler
	public void b(BlockBreakEvent event){
		String level = event.getPlayer().getLevel().getName();
		double x = event.getBlock().x;
		double y = event.getBlock().y;
		double z = event.getBlock().z;
		String xyz ="x"+x+"y"+y+"z"+z;
		Player player = event.getPlayer();
		String who = player.getName();
		String ip =  player.getAddress();
		String cid = player.getClientSecret();
		String action = "Break";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = sdf1.toString();
		String blockname = event.getBlock().getName();
		String blockid = String.valueOf(event.getBlock().getId());
		String meta = String.valueOf(event.getBlock().getDamage());
////
		Map<String, String> map = new HashMap<String, String>();
		map.put("who",who);
		map.put("ip",ip);
		map.put("cid",cid);
		map.put("action",action);
		map.put("time",time);
		map.put("blockname",blockname);
		map.put("blockid",blockid);
		map.put("meta",meta);
		map.put("level",level);
////
		String data= new JSONObject(map).toString();
		String encoded = Base64.getEncoder()
                .encodeToString(data.getBytes());
		config.set(xyz,encoded);
		config.save();
	}

	@EventHandler
	public void p(BlockPlaceEvent event){
		String level = event.getPlayer().getLevel().getName();
		double x = event.getBlock().x;
		double y = event.getBlock().y;
		double z = event.getBlock().z;
		String xyz ="x"+x+"y"+y+"z"+z;
		Player player = event.getPlayer();
		String who = player.getName();
		String ip =  player.getAddress();
		String cid = player.getClientSecret();
		String action = "Place";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String time = sdf1.toString();
		String blockname = event.getBlock().getName();
		String blockid = String.valueOf(event.getBlock().getId());
		String meta = String.valueOf(event.getBlock().getDamage());
////
		Map<String, String> map = new HashMap<String, String>();
		map.put("who",who);
		map.put("ip",ip);
		map.put("cid",cid);
		map.put("action",action);
		map.put("time",time);
		map.put("blockname",blockname);
		map.put("blockid",blockid);
		map.put("meta",meta);
		map.put("level",level);
////
		String data= new JSONObject(map).toString();
		String encoded = Base64.getEncoder()
                .encodeToString(data.getBytes());
		config.set(xyz,encoded);
		config.save();
	}



}
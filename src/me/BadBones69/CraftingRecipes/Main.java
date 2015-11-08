package me.BadBones69.CraftingRecipes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	@Override
	public void onDisable() {
	}
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getServer().clearRecipes();
		addItems();
	}
	public void addItems(){
		for(String item : getConfig().getConfigurationSection("Items").getKeys(false)){
			Material type = Material.matchMaterial(getConfig().getString("Items." + item + ".Item"));
			ItemStack i = new ItemStack(type);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(color(getConfig().getString("Items." + item + ".Name")));
			List<String> l = getConfig().getStringList("Items." + item + ".Lore");
			List<String> lore = new ArrayList<String>();
			for(String L : l){
				L = color(L);
				lore.add(L);
			}
			m.setLore(lore);
			i.setItemMeta(m);
			for(String e : getConfig().getStringList("Items." + item + ".Enchantments")){
				String[] breakdown = e.split(":");
				String enchantment = breakdown[0];
				int lvl = Integer.parseInt(breakdown[1]);
				i.addUnsafeEnchantment(Enchantment.getByName(enchantment), lvl);
			}
			List<String> r = getConfig().getStringList("Items." + item + ".ItemCrafting");
			String line1 = r.get(0);
			String line2 = r.get(1);
			String line3 = r.get(2);
			ShapedRecipe R = new ShapedRecipe(i);
			R.shape(line1,
					line2,
					line3);
			for(String I : getConfig().getStringList("Items." + item + ".Ingredients")){
				String[] breakdown = I.split(":");
				char lin1 = breakdown[0].charAt(0);
				String lin2 = breakdown[1];
				Material mi = Material.matchMaterial(lin2);
				R.setIngredient(lin1, mi);
			}
			Bukkit.getServer().addRecipe(R);
		}
	}
	String color(String msg){
		msg = msg.replaceAll("(&([a-f0-9]))", "\u00A7$2");
		msg = msg.replaceAll("&l", ChatColor.BOLD + "");
		msg = msg.replaceAll("&o", ChatColor.ITALIC + "");
		msg = msg.replaceAll("&k", ChatColor.MAGIC + "");
		msg = msg.replaceAll("&n", ChatColor.UNDERLINE + "");
		return msg;
	}
}
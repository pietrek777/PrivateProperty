package pl.mccode.privateproperty.protect;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;
import org.bukkit.material.Sign;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import pl.mccode.privateproperty.Main;
import pl.mccode.privateproperty.config.ConfigProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Protection {
	private static final String PRIVATE_SIGN_COMMAND = "[Private]";
	private static final String PRIVATE_SIGN_TEXT = ChatColor.DARK_BLUE + PRIVATE_SIGN_COMMAND;
	private static final String OWNER_SIGN_TEXT = "O: ";
	private static final String OWNER_UUID_META_KEY = "ownerUUID";

	public static final Material[] PROTECTABLE = {
			Material.WALL_SIGN,

			Material.DISPENSER,
			Material.FURNACE,
			Material.BURNING_FURNACE,
			Material.CHEST,
			Material.TRAPPED_CHEST,
			Material.HOPPER,
			Material.DROPPER,
			Material.DARK_OAK_DOOR,
			Material.ACACIA_DOOR,
			Material.BIRCH_DOOR,
			Material.JUNGLE_DOOR,
			Material.SPRUCE_DOOR,
			Material.WOOD_DOOR,
			Material.WOODEN_DOOR,
			Material.BREWING_STAND,
			Material.BEACON,
			Material.COMMAND_CHAIN,
			Material.COMMAND_REPEATING,
			Material.COMMAND,
			Material.ENCHANTMENT_TABLE,
	};

	public static void createProtection(SignChangeEvent event){
		Block blockPlaced = event.getBlock();
		if(blockPlaced.getType() == Material.WALL_SIGN && event.getLine(0).equalsIgnoreCase(PRIVATE_SIGN_COMMAND)){

			Player player = event.getPlayer();
			Block blockBehind = getBlockBehindSign(blockPlaced);
			String materialName = getFriendlyBlockName(blockBehind);
			if(isProtectable(blockBehind)){
				if(blockBehind.getMetadata(OWNER_UUID_META_KEY).size() == 0){
					ProtectedResource protectedResource = new ProtectedResource();
					if(blockBehind.getState() instanceof Door){
						protectedResource.setSingle(false);
					}
					else if(blockBehind.getState() instanceof Chest){
						Chest chest = (Chest) blockBehind.getState();
						if(chest.getInventory().getHolder() instanceof DoubleChest){
							protectedResource.setSingle(false);
						} else protectedResource.setSingle(true);
					}
					else{
						protectedResource.setSingle(true);
					}

					event.setLine(0, PRIVATE_SIGN_TEXT);
					event.setLine(1, OWNER_SIGN_TEXT + event.getPlayer().getName());

					protectedResource
							.setBlockLocation(blockBehind.getLocation())
							.setSignLocation(blockPlaced.getLocation())
							.setOwnerUUID(player.getUniqueId());

					initializeProtection(protectedResource);

					//Save data to file
					FileConfiguration protectedConfig = ConfigProvider.getInstance().getProtectedFileConfiguration();

					try{

						List<ProtectedResource> list = (ArrayList<ProtectedResource>) protectedConfig.getList(ConfigProvider.PROTECTED_BLOCKS_KEY);
						list.add(protectedResource);
						protectedConfig.set(ConfigProvider.PROTECTED_BLOCKS_KEY, list);
						ConfigProvider.getInstance().saveProtectedConfig();
						sendSuccessMessage(player, materialName);
					} catch(Exception e){
						Main.getInstance().printError("Invalid data!");
					}

				}
				else{
					sendAlreadyProtectedMessage(player, materialName);
					blockPlaced.breakNaturally();
				}

			}
			else{
				sendFailureMessage(player, materialName);
				blockPlaced.breakNaturally();
			}
		}
	}
	public static void checkPermissions(PlayerInteractEvent event){
		Block clickedBlock = event.getClickedBlock();
		if (isProtectable(clickedBlock)){
			UUID ownerUUID = getOwnerUUID(clickedBlock);
			if(ownerUUID != null) {
				if ((!ownerUUID.equals(event.getPlayer().getUniqueId()))) {
					event.setCancelled(true);
					sendNoPermissionMessage(event.getPlayer(), getFriendlyBlockName(clickedBlock));
				}
			}
		}
	}
	private static UUID getOwnerUUID(Block block){
		for (MetadataValue mv : block.getMetadata(OWNER_UUID_META_KEY)) {
			if (mv.getOwningPlugin() == Main.getInstance()) {
				if(mv.value() instanceof UUID) return (UUID) mv.value();
			}
		}
		return null;
	}

	private static String getFriendlyBlockName(Block block){
		return block.getType().name().toLowerCase().replace('_', ' ');
	}
	private static Block getBlockBehindSign(Block block){
		Sign sign = (Sign) block.getState().getData();
		return block.getRelative(sign.getAttachedFace());
	}
	private static void sendSuccessMessage(Player player, String material){
		player.sendMessage(Main.PLUGIN_PREFIX + "Private " + material + " created sucessfully");
	}
	private static void sendFailureMessage(Player player, String material){
		player.sendMessage(Main.PLUGIN_PREFIX + "You can't create protection for " + material + "!");
	}
	private static void sendAlreadyProtectedMessage(Player player, String material){
		player.sendMessage(Main.PLUGIN_PREFIX + "This " + material + " is already protected!");
	}
	private static void sendNoPermissionMessage(Player player, String material){
		player.sendMessage(Main.PLUGIN_PREFIX + "This " + material + " is not yours!");
	}
	private static boolean isProtectable(Block block){
		return Stream.of(PROTECTABLE).anyMatch(x -> x == block.getType());
	}

	private static String createStringCords(Location location){
		return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
	}

	public static void initializeProtection(ProtectedResource protectedResource){
		Block block = protectedResource.getBlockLocation().getBlock();
		Block sign = protectedResource.getSignLocation().getBlock();

		UUID uuid = protectedResource.getOwnerUUID();

		saveUUIDToMetadata(sign, uuid);
		if(protectedResource.isSingle()){
			saveUUIDToMetadata(block, uuid);
		}
		else {
			BlockState state = block.getState();
			if(state instanceof Chest){
				Chest chest = (Chest) state;
				if(chest.getInventory().getHolder() instanceof DoubleChest){
					DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
					Block leftChest = ((Chest) doubleChest.getLeftSide()).getBlock();
					Block rightChest = ((Chest) doubleChest.getRightSide()).getBlock();

					saveUUIDToMetadata(leftChest, uuid);
					saveUUIDToMetadata(rightChest, uuid);
				}
			}
			else if(state instanceof Door){
				Door door = (Door) state;
				Block doorTop, doorBottom;
				if(door.isTopHalf()){
					doorTop = block;
					doorBottom = block.getRelative(BlockFace.DOWN);
				} else {
					doorTop = block.getRelative(BlockFace.UP);
					doorBottom = block;
				}
				saveUUIDToMetadata(doorTop, uuid);
				saveUUIDToMetadata(doorBottom, uuid);
			}
		}

	}
	private static void saveUUIDToMetadata(Block block, Player player){
		saveUUIDToMetadata(block, player.getUniqueId());
	}
	private static void saveUUIDToMetadata(Block block, UUID uuid){
		block.setMetadata(OWNER_UUID_META_KEY, new FixedMetadataValue(Main.getInstance(), uuid));
	}
}

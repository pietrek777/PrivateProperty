package pl.mccode.privateproperty.protect;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import pl.mccode.privateproperty.Main;
import pl.mccode.privateproperty.util.LocationUtils;

import java.io.File;
import java.util.*;

public class ProtectedResource implements ConfigurationSerializable{
	
	//constants
	private static final String BLOCK_LOCATION_KEY = "block-loc";
	private static final String SIGN_LOCATION_KEY = "sign-loc";
	private static final String OWNER_UUID_KEY = "uuid";
	private static final String RESOURCE_TYPE_KEY = "type";

	public static final String SINGLE = "single";
	public static final String DOUBLE_CHEST = "double_chest";
	public static final String DOOR = "door";


	//fields
	private String blockLocation;
	private String signLocation;

	private String ownerUUID;
	private String resourceType;


	//getters and setters
	public String getBlockLocation() {
		return blockLocation;
	}

	public ProtectedResource setBlockLocation(String blockLocation) {
		this.blockLocation = blockLocation;
		return this;
	}
	public ProtectedResource setBlockLocation(Location blockLocation) {
		this.blockLocation = LocationUtils.stringFromLocation(blockLocation);
		return this;
	}

	public String getSignLocation() {
		return signLocation;
	}

	public ProtectedResource setSignLocation(String signLocation) {
		this.signLocation = signLocation;
		return this;
	}
	public ProtectedResource setSignLocation(Location signLocation) {
		this.signLocation = LocationUtils.stringFromLocation(signLocation);
		return this;
	}

	public String getOwnerUUID() {
		return ownerUUID;
	}

	public ProtectedResource setOwnerUUID(String ownerUUID) {
		this.ownerUUID = ownerUUID;
		return this;
	}

	public String getResourceType() {
		return resourceType;
	}

	public ProtectedResource setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	//serialization
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put(BLOCK_LOCATION_KEY, blockLocation);
		map.put(SIGN_LOCATION_KEY, signLocation);
		map.put(OWNER_UUID_KEY, ownerUUID);
		map.put(RESOURCE_TYPE_KEY, resourceType);
		return map;
	}

	//constructors
	public ProtectedResource(Map<String, Object> map){
		this.setBlockLocation((String) map.get(BLOCK_LOCATION_KEY))
				.setSignLocation((String) map.get(SIGN_LOCATION_KEY))
				.setOwnerUUID((String) map.get(OWNER_UUID_KEY))
				.setResourceType((String) map.get(RESOURCE_TYPE_KEY));
	}

	public ProtectedResource(String blockLocation, String signLocation, String ownerUUID, String resourceType) {
		this.blockLocation = blockLocation;
		this.signLocation = signLocation;
		this.ownerUUID = ownerUUID;
		this.resourceType = resourceType;
	}

	public ProtectedResource(Location blockLocation, Location signLocation, String ownerUUID, String resourceType) {
		this.blockLocation = LocationUtils.stringFromLocation(blockLocation);
		this.signLocation = LocationUtils.stringFromLocation(signLocation);
		this.ownerUUID = ownerUUID;
		this.resourceType = resourceType;
	}

	public ProtectedResource(Location blockLocation, Location signLocation, UUID ownerUUID, String resourceType) {
		this.blockLocation = LocationUtils.stringFromLocation(blockLocation);
		this.signLocation = LocationUtils.stringFromLocation(signLocation);
		this.ownerUUID = ownerUUID.toString();
		this.resourceType = resourceType;
	}

	public ProtectedResource(){}


	//overridden methods
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProtectedResource)) return false;
		ProtectedResource resource = (ProtectedResource) o;
		return Objects.equals(getBlockLocation(), resource.getBlockLocation()) &&
				Objects.equals(getSignLocation(), resource.getSignLocation()) &&
				Objects.equals(getOwnerUUID(), resource.getOwnerUUID()) &&
				Objects.equals(resourceType, resource.resourceType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBlockLocation(), getSignLocation(), getOwnerUUID(), resourceType);
	}

	@Override
	public String toString() {
		return "ProtectedResource{" +
				"blockLocation='" + blockLocation + '\'' +
				", signLocation='" + signLocation + '\'' +
				", ownerUUID='" + ownerUUID + '\'' +
				", resourceType='" + resourceType + '\'' +
				'}';
	}
}

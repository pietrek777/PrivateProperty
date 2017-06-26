package pl.mccode.privateproperty.protect;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ProtectedResource implements ConfigurationSerializable{

	private Location blockLocation;
	private Location signLocation;

	private UUID ownerUUID;
	private boolean single;

	public Location getBlockLocation() {
		return blockLocation;
	}

	public ProtectedResource setBlockLocation(Location blockLocation) {
		this.blockLocation = blockLocation;
		return this;
	}

	public Location getSignLocation() {
		return signLocation;
	}

	public ProtectedResource setSignLocation(Location signLocation) {
		this.signLocation = signLocation;
		return this;
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public ProtectedResource setOwnerUUID(UUID ownerUUID) {
		this.ownerUUID = ownerUUID;
		return this;
	}

	public boolean isSingle() {
		return single;
	}

	public ProtectedResource setSingle(boolean single) {
		this.single = single;
		return this;
	}

	public ProtectedResource(Location blockLocation, Location signLocation, UUID ownerUUID, boolean single) {
		this.blockLocation = blockLocation;
		this.signLocation = signLocation;
		this.ownerUUID = ownerUUID;
		this.single = single;
	}
	public ProtectedResource() {}

	public ProtectedResource(Map<String, Object> map){
		this.setBlockLocation((Location) map.get("blockLocation"))
				.setSignLocation((Location) map.get("signLocation"))
				.setOwnerUUID((UUID) map.get("ownerUUID"))
				.setSingle((boolean) map.get("single"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProtectedResource)) return false;
		ProtectedResource that = (ProtectedResource) o;
		return isSingle() == that.isSingle() &&
				Objects.equals(getBlockLocation(), that.getBlockLocation()) &&
				Objects.equals(getSignLocation(), that.getSignLocation()) &&
				Objects.equals(getOwnerUUID(), that.getOwnerUUID());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBlockLocation(), getSignLocation(), getOwnerUUID(), isSingle());
	}

	@Override
	public String toString() {
		return "ProtectedResource{" +
				"blockLocation=" + blockLocation +
				", signLocation=" + signLocation +
				", ownerUUID=" + ownerUUID +
				", single=" + single +
				'}';
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("blockLocation", blockLocation);
		map.put("signLocation", signLocation);
		map.put("ownerUUID", ownerUUID);
		map.put("single", single);
		return map;
	}
}

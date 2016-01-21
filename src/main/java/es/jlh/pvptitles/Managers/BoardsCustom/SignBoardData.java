package es.jlh.pvptitles.Managers.BoardsCustom;

import es.jlh.pvptitles.Managers.BoardsAPI.BoardData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;

/**
 *
 * @author AlternaCraft
 */
public class SignBoardData extends BoardData {

    private final Sign matSign = new Sign(Material.WALL_SIGN);

    private static final String XP = "X+";
    private static final String XN = "X-";
    private static final String ZP = "Z+";
    private static final String ZN = "Z-";

    public static final short NORTH = 1;
    public static final short SOUTH = 2;
    public static final short EAST = 3;
    public static final short WEST = 4;
    
    // Orientacion
    private boolean xp = false;
    private boolean xn = false;
    private boolean zp = false;
    private boolean zn = false;
    
    // BlockFace
    private short blockface = 0;    

    public SignBoardData(String nombre, String modelo, String server, Location l) {
        super(l);
        this.nombre = nombre;
        this.modelo = modelo;
        this.server = server;
    }
    
    public void setOrientacion(String o) {
        switch(o) {
            case XP:
                xp = true;
                break;
            case XN:
                xn = true;
                break;
            case ZP:
                zp = true;
                break;
            case ZN:
                zn = true;
            default:
                break;
        }
    }
    
    public String getOrientacion() {
        if (xp) {
            return XP;
        }
        else if (xn) {
            return XN;
        }
        else if (zp) {
            return ZP;
        }
        else if (zn) {
            return ZN;
        }
        
        return null;
    }

    public boolean isXp() {
        return xp;
    }

    public boolean isXn() {
        return xn;
    }

    public boolean isZp() {
        return zp;
    }

    public boolean isZn() {
        return zn;
    }
    
    public Sign getSignMaterial() {        
        return this.matSign;
    }

    public BlockFace getBlockface() {
        switch (this.blockface) {
            case NORTH: 
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.WEST;
            default:
                return null;
        }
    }

    public short getPrimitiveBlockface() {
        return this.blockface;
    }
    
    public void setBlockface(short blockface) {
        this.blockface = blockface;
        this.matSign.setFacingDirection(this.getBlockface());
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }
}
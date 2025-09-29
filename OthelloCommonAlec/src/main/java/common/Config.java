package common;

import java.io.Serializable;

/**
 * Author: Alec Waumans (2025)
 * Configuration object for an Othello game session.
 * Contains board size, CPU mode flag, and smart CPU strategy flag.
 * This object is serializable so it can be transferred between the client and server.
 *
 */
public class Config implements Serializable {

    /** Serialization identifier for network transmission. */
    private static final long serialVersionUID = 1L;
    
    /** Size of the Othello board (e.g., 8 for an 8x8 board). */
    private final int boardSize;

    /** Whether the game should be played against a CPU. */
    private final boolean cpuMode;

    /** Whether the CPU should use the "smart" strategy. */
    private final boolean smartCPU;

    /**
     * Constructs a new configuration object for the game.
     *
     * @param boardSize the size of the game board
     * @param cpuMode   true if the game is against a CPU
     * @param smartCPU  true if the CPU uses the smart strategy
     */
    public Config(int boardSize, boolean cpuMode, boolean smartCPU) {
        this.boardSize = boardSize;
        this.cpuMode = cpuMode;
        this.smartCPU = smartCPU;
    }

    /** @return the board size */
    public int getBoardSize() {
        return boardSize;
    }

    /** @return true if the game is in CPU mode */
    public boolean isCpuMode() {
        return cpuMode;
    }

    /** @return true if the CPU uses the smart strategy */
    public boolean isSmartCPU() {
        return smartCPU;
    }

    /**
     * Returns a string representation of the configuration.
     * Useful for debugging and logging.
     */
    @Override
    public String toString() {
        return "Config{" +
                "boardSize=" + boardSize +
                ", cpuMode=" + cpuMode +
                ", smartCPU=" + smartCPU +
                '}';
    }
}

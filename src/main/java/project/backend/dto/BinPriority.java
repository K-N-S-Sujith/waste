package project.backend.dto;

import project.backend.model.WasteBin;

public class BinPriority {
    private WasteBin bin;
    private double score;

    public BinPriority(WasteBin bin, double score) {
        this.bin = bin;
        this.score = score;
    }

    public WasteBin getBin() {
        return bin;
    }

    public double getScore() {
        return score;
    }
}
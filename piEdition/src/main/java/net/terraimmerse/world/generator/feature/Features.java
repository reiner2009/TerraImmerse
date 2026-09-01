package net.terraimmerse.world.generator.feature;

public class Features {
    public static TreeFeature treeFeature;
    public static void initFeatures(){
        treeFeature=new TreeFeature();
        treeFeature.init();
    }
}

package net.semanticmetadata.lire.imageanalysis.features.global;

import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.LireFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.mpeg7.DominantColorImpl;

import java.awt.image.BufferedImage;

public class DominantColor extends DominantColorImpl implements GlobalFeature {
    RGBA rgba;
    double relativeAmmount;

    @Override
    public void extract(BufferedImage image) {

        int imprecision = 32;       // r/imprecision g/imprecision b/imprecision a/imprecision
        int numOfPoints = 10000;
        Pair<RGBA, Double> maxEntry = getDominantRGBA(image, imprecision, numOfPoints);

        rgba = maxEntry.key;
        relativeAmmount = maxEntry.value;
    }

    @Override
    public double[] getFeatureVector() {
        return new double[] {rgba.r, rgba.g, rgba.b, rgba.a, relativeAmmount};
    }

    @Override
    public String getFeatureName() {
        return "Custom Dominant Color";
    }

    @Override
    public String getFieldName() {
        return "customDominantColor";
    }

    @Override
    public byte[] getByteArrayRepresentation() {
        byte[] result = new byte[5];
        result[0] = (byte)rgba.r;
        result[1] = (byte)rgba.g;
        result[2] = (byte)rgba.b;
        result[3] = (byte)rgba.a;
        result[4] = (byte)relativeAmmount;

        return result;
    }

    @Override
    public void setByteArrayRepresentation(byte[] featureData) {
        setByteArrayRepresentation(featureData, 0, 0);
    }

    @Override
    public void setByteArrayRepresentation(byte[] featureData, int offset, int length) {
        int r = (int)featureData[0 + offset];
        int g = (int)featureData[1 + offset];
        int b = (int)featureData[2 + offset];
        int a = (int)featureData[3 + offset];
        relativeAmmount = (double)featureData[4 + offset];
        rgba = new RGBA(r, g, b, a);
    }

    @Override
    public double getDistance(LireFeature descriptor) {
        if (!(descriptor instanceof DominantColor)) return -1d;
        return 40 * getSimilarity(rgba, relativeAmmount, ((DominantColor) descriptor).rgba, ((DominantColor) descriptor).relativeAmmount);
    }
}

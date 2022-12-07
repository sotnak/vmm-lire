package net.semanticmetadata.lire.imageanalysis.features.global.mpeg7;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class DominantColorImpl {

    protected class Pair<T, S>{
        public T key;
        public S value;
        public Pair(T key, S value){
            this.key = key;
            this.value = value;
        }
    }
    protected class RGBA implements Comparable<RGBA>{
        public int r;
        public int g;
        public int b;
        public int a;

        public RGBA(int r, int g, int b, int a){
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        @Override
        public int compareTo(RGBA o) {
            if(o.a == a && o.g == g && o.r == r && o.b == b)
                return 0;

            int sum = r + g + b + a;
            int sumo = o.r + o.g + o.b + o.a;

            if(sum > sumo)
                return 1;

            return -1;
        }

        @Override
        public String toString(){
            return String.format("r: %d, g: %d, b: %d, a: %d", r, g, b, a);
        }

        @Override
        public int hashCode() {
            return r + g + b + a;
        }

        @Override
        public boolean equals(Object obj) {
            if( !(obj instanceof RGBA) )
                return false;

            return compareTo((RGBA) obj) == 0;
        }
    }

    protected Pair<RGBA, Double> getDominantRGBA(BufferedImage image, int imprecision, int numOfPoints){
        HashMap<RGBA, Long> colorHist = new HashMap<>();
        int height = image.getHeight(), width = image.getWidth();

        int rowSize = (int)Math.sqrt(numOfPoints);
        int columnSize = (int)Math.sqrt(numOfPoints);

        int rowJump = (width >= rowSize ? width / rowSize: 1);
        int columnJump = (height >= columnSize ? height / columnSize: 1);

        for (int y = 0; y < height; y+=columnJump) {
            for (int x = 0; x < width; x+=rowJump) {
                int colors = image.getRGB(x, y);
                int alpha = (colors >> 24) & 255;
                int red = (colors >> 16) & 255;
                int green = (colors >> 8) & 255;
                int blue = colors & 255;

                RGBA rgba = new RGBA(red/imprecision, green/imprecision, blue/imprecision, alpha/imprecision);

                if(colorHist.containsKey(rgba)){
                    colorHist.put(rgba, colorHist.get(rgba) + 1);
                } else {
                    colorHist.put(rgba, 1L);
                }
            }
        }

        Map.Entry<RGBA, Long> maxEntry = null;

        for (Map.Entry<RGBA, Long> entry : colorHist.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        assert maxEntry != null;

        return new Pair<>(maxEntry.getKey(), (maxEntry.getValue().floatValue())/((double)numOfPoints));
    }

    protected double getSimilarity(RGBA rgba, double ra, RGBA rgbao, double rao){
        return Math.sqrt( Math.pow(rgba.r - rgbao.r, 2)
                + Math.pow(rgba.g - rgbao.g, 2)
                + Math.pow(rgba.b - rgbao.b, 2)
                + Math.pow(rgba.a - rgbao.a, 2)
                + Math.pow(ra - rao, 2) );
    }
}

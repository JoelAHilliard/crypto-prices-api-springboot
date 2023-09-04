package com.nssybackend.api.util;


import java.io.StringWriter;
import java.util.List;
public class CryptoSVGGenerator {
    public static String generateSVG(List<Object[]> hourlyPrices) {
        StringBuilder svg = new StringBuilder();
        double svgHeight = 45.0;
        double svgWidth = 100.0;

        String color = "red";

        Object firstPriceObject = hourlyPrices.get(0)[0];
        Object lastPriceObject = hourlyPrices.get(hourlyPrices.size()-1)[0];

        double firstPrice = 0.0;
        double lastPrice = 0.0;

        if (firstPriceObject instanceof Integer) {
            firstPrice = ((Integer) firstPriceObject).doubleValue();
        } else if (firstPriceObject instanceof Double) {
            firstPrice = ((Double) firstPriceObject);
        }
        if (lastPriceObject instanceof Integer) {
            lastPrice = ((Integer) lastPriceObject).doubleValue();
        } else if (lastPriceObject instanceof Double) {
            lastPrice = (Double) lastPriceObject;
        }

        if(firstPrice > lastPrice){
            color ="green";
        }
        
        // Find min and max price for normalization
        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;
        for (Object[] data : hourlyPrices) {
            double price = ((Number) data[0]).doubleValue();
            minPrice = Math.min(minPrice, price);
            maxPrice = Math.max(maxPrice, price);
        }
        
        

        // Begin the SVG element
        svg.append("<svg width=\"").append(svgWidth).append("\" height=\"").append(svgHeight).append("\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        
        // Draw lines between points
        double previousX = 0, previousY = 0;
        for (int i = 0; i < hourlyPrices.size(); i++) {
            double x = i * 10; // Spacing of 10 units between points
            
            // Normalize the price to fit within SVG height
            double price = ((Number) hourlyPrices.get(i)[0]).doubleValue();
            double y = normalize(price, minPrice, maxPrice, 0, svgHeight);
            
            if (i != 0) {
                svg.append(String.format("  <line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" stroke=\""+color+"\"/>\n", previousX, svgHeight - previousY, x, svgHeight - y));
            }
            previousX = x;
            previousY = y;
        }
        
        // End the SVG element
        svg.append("</svg>");
        
        return svg.toString();
    }
    // Normalize function
    public static double normalize(double value, double min, double max, double newMin, double newMax) {
        return newMin + ((value - min) / (max - min)) * (newMax - newMin);
    }
}
package com.nssybackend.api.util;


import java.io.StringWriter;
import java.util.List;
public class CryptoSVGGenerator {
    public static String generateSVG(List<Object[]> hourlyPrices, String color) {
        StringBuilder svg = new StringBuilder();
        StringBuilder points = new StringBuilder();
        double svgHeight = 45.0;
        double svgWidth = 100.0;

        double minPrice = Double.MAX_VALUE;
        double maxPrice = Double.MIN_VALUE;

        for (Object[] data : hourlyPrices) {
            double price = ((Number) data[0]).doubleValue();
            minPrice = Math.min(minPrice, price);
            maxPrice = Math.max(maxPrice, price);
        }

        // Begin the SVG element
        svg.append("<svg width=\"").append(svgWidth).append("\" height=\"").append(svgHeight).append("\" xmlns=\"http://www.w3.org/2000/svg\">\n");

        for (int i = 0; i < hourlyPrices.size(); i++) {
            double x = i * 10; // Spacing of 10 units between points

            // Normalize the price to fit within SVG height
            double price = ((Number) hourlyPrices.get(i)[0]).doubleValue();
            double y = normalize(price, minPrice, maxPrice, 0, svgHeight);

            // Append points for polyline
            points.append(String.format("%.2f,%.2f ", x, svgHeight - y));
        }

        // Draw polyline
        svg.append("<polyline points=\"").append(points.toString()).append("\" stroke=\"").append(color).append("\" fill=\"none\"/>\n");

        // End the SVG element
        svg.append("</svg>");

        return svg.toString();
    }
    // Normalize function
    public static double normalize(double value, double min, double max, double newMin, double newMax) {
        return newMin + ((value - min) / (max - min)) * (newMax - newMin);
    }
}
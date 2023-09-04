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

    // Begin the SVG element (Removed whitespaces)
    svg.append("<svg width=\"").append((int) svgWidth).append("\" height=\"").append((int) svgHeight).append("\" xmlns=\"http://www.w3.org/2000/svg\">");

    for (int i = 0; i < hourlyPrices.size(); i++) {
        double x = i * 10;

        double price = ((Number) hourlyPrices.get(i)[0]).doubleValue();
        double y = normalize(price, minPrice, maxPrice, 0, svgHeight);

        // Append points for polyline (Reduced to 1 decimal point)
        points.append(String.format("%.1f,%.1f", x, svgHeight - y));
        if (i != hourlyPrices.size() - 1) {
            points.append(" ");
        }
    }

    // Draw polyline (Removed whitespaces)
    svg.append("<polyline points=\"").append(points.toString()).append("\" stroke=\"").append(color).append("\" fill=\"none\"/>");

    // End the SVG element (Removed whitespaces)
    svg.append("</svg>");

    return svg.toString();
}

    // Normalize function
    public static double normalize(double value, double min, double max, double newMin, double newMax) {
        // Add some padding to min and max values
        return newMin + ((value - min) / (max - min)) * (newMax - newMin);
    }

}
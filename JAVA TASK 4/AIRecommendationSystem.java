import java.util.HashMap;
import java.util.Map;

public class AIRecommendationSystem {

    public static void main(String[] args) {
        // Sample data: User preferences for items
        Map<Integer, Map<Integer, Double>> userItemPreferences = new HashMap<>();

        // Adding user preferences
        userItemPreferences.put(1, Map.of(101, 5.0, 102, 3.0, 103, 2.5));
        userItemPreferences.put(2, Map.of(101, 2.0, 102, 2.5, 103, 5.0, 104, 4.0));
        userItemPreferences.put(3, Map.of(101, 5.0, 102, 3.0, 104, 4.5));

        int targetUser = 1; // User for whom we need recommendations

        // Generate recommendations
        Map<Integer, Double> recommendations = getRecommendations(targetUser, userItemPreferences);

        // Display recommendations
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available for User " + targetUser);
        } else {
            System.out.println("Recommendations for User " + targetUser + ":");
            for (Map.Entry<Integer, Double> entry : recommendations.entrySet()) {
                System.out.println("Item ID: " + entry.getKey() + " | Score: " + entry.getValue());
            }
        }
    }

    private static Map<Integer, Double> getRecommendations(int userId, Map<Integer, Map<Integer, Double>> userItemPreferences) {
        Map<Integer, Double> recommendations = new HashMap<>();

        // Get target user's preferences
        Map<Integer, Double> targetPreferences = userItemPreferences.get(userId);

        for (Map.Entry<Integer, Map<Integer, Double>> entry : userItemPreferences.entrySet()) {
            int otherUser = entry.getKey();

            // Skip the target user
            if (otherUser == userId) continue;

            // Calculate similarity
            double similarity = calculateSimilarity(targetPreferences, entry.getValue());

            // Add weighted scores for recommendations
            for (Map.Entry<Integer, Double> itemEntry : entry.getValue().entrySet()) {
                int item = itemEntry.getKey();
                double score = itemEntry.getValue();

                // Recommend items the target user hasn't rated
                if (!targetPreferences.containsKey(item)) {
                    recommendations.put(item, recommendations.getOrDefault(item, 0.0) + similarity * score);
                }
            }
        }

        return recommendations;
    }

    private static double calculateSimilarity(Map<Integer, Double> prefs1, Map<Integer, Double> prefs2) {
        double dotProduct = 0.0, norm1 = 0.0, norm2 = 0.0;

        for (int item : prefs1.keySet()) {
            if (prefs2.containsKey(item)) {
                dotProduct += prefs1.get(item) * prefs2.get(item);
            }
            norm1 += Math.pow(prefs1.get(item), 2);
        }

        for (double value : prefs2.values()) {
            norm2 += Math.pow(value, 2);
        }

        return norm1 > 0 && norm2 > 0 ? dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2)) : 0.0;
    }
}

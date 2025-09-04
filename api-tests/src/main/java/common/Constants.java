package common;

public class Constants {
    
    public static final String SPECIAL_API_KEY = "special-key";
    public static final String BASE_URI = "https://petstore.swagger.io/v2";
    
    // Test Order IDs
    public static final int VALID_ORDER_ID_FOR_TESTING = 5;
    public static final int ORDER_ID_UPPER_LIMIT = 10;
    public static final int ORDER_ID_ABOVE_LIMIT = 11;
    public static final int ORDER_ID_ZERO = 0;
    public static final int ORDER_ID_NEGATIVE = -5;
    
    // HTTP Status Codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    
    // API Response Values
    public static final String ERROR_TYPE = "error";
    public static final String UNKNOWN_TYPE = "unknown";
    
    // Order Statuses
    public static final String STATUS_PLACED = "placed";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_DELIVERED = "delivered";
    
    // Performance Thresholds
    public static final long MAX_RESPONSE_TIME_MS = 5000;
    public static final long MAX_CONCURRENT_TIME_MS = 10000;
    
    // Inventory Fields
    public static final String AVAILABLE_FIELD = "available";
}

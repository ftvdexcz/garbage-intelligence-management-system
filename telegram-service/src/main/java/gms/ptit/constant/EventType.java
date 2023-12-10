package gms.ptit.constant;

public enum EventType {
    EVENT_TYPE_LOAD_CELL(0, "Cân nặng"),
    EVENT_TYPE_INVALID_PLATE_DETECT(1, "Cảnh báo xe không hợp lệ"),

    EVENT_TYPE_ALL(2, "Tất cả sự kiện");

    public final int code;
    public final String message;
    EventType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

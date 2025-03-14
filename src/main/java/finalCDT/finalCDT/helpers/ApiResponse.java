package finalCDT.finalCDT.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean ok;
    private String message;
    private T data;
    
    public ApiResponse(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
        this.data = null;
    }
}

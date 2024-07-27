package finalCDT.finalCDT.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T, K>{
    private boolean ok;
    private T data;
    private K message;
}

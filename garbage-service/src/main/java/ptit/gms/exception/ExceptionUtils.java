package ptit.gms.exception;

import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class ExceptionUtils {
    public static boolean isTraceOn(WebRequest request) {
        String [] value = request.getParameterValues("trace");
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

    public static String getStackTrace(Exception err){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        err.printStackTrace(pw);
        return sw.toString();
    }
}

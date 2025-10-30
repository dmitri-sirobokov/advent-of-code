#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};

#end

import java.util.List;

#set ($MyYear=${PACKAGE_NAME.substring(${PACKAGE_NAME.lastIndexOf(".")}+1)})
/**
 * ${NAME}:
 * 
 */
public class ${NAME} {
    public static long part1(List<String> input) {
        return 0L;
    }

    public static long part2(List<String> input) {
        return 0L;
    }
}

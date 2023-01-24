package inv.entity;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String name();
	String display_name();
	boolean is_primary() default false;
	boolean is_search() default false;
}

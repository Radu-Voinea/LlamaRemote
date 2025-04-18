package org.jetbrains.plugins.template;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.DynamicBundle;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

@Getter
public final class LLamaRemote extends DynamicBundle {

	@Getter
	@Accessors(fluent = true)
	private static LLamaRemote instance;

	private final Gson gson = new GsonBuilder().create();

	@NonNls
	private static final String BUNDLE = "messages.LLamaRemote";
	private static final LLamaRemote INSTANCE = new LLamaRemote();

	private LLamaRemote() {
		super(BUNDLE);

		instance = this;
	}

	public static String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
		return INSTANCE.getMessage(key, params);
	}

//    public static Supplier<String> messagePointer(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params) {
//        return INSTANCE.getLazyMessage(key, params);
//    }
}

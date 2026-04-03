package dev.muffar.moneyfikasi.shortcut.list;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ShortcutViewModel_Factory implements Factory<ShortcutViewModel> {
  @Override
  public ShortcutViewModel get() {
    return newInstance();
  }

  public static ShortcutViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ShortcutViewModel newInstance() {
    return new ShortcutViewModel();
  }

  private static final class InstanceHolder {
    static final ShortcutViewModel_Factory INSTANCE = new ShortcutViewModel_Factory();
  }
}

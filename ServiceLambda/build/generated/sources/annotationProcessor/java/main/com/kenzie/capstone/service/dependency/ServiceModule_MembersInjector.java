package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.dao.ExampleDao;
import dagger.MembersInjector;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ServiceModule_MembersInjector implements MembersInjector<ServiceModule> {
  private final Provider<ExampleDao> exampleDaoProvider;

  public ServiceModule_MembersInjector(Provider<ExampleDao> exampleDaoProvider) {
    this.exampleDaoProvider = exampleDaoProvider;
  }

  public static MembersInjector<ServiceModule> create(Provider<ExampleDao> exampleDaoProvider) {
    return new ServiceModule_MembersInjector(exampleDaoProvider);
  }

  @Override
  public void injectMembers(ServiceModule instance) {
    injectProvideLambdaService(instance, exampleDaoProvider.get());
  }

  public static LambdaService injectProvideLambdaService(ServiceModule instance,
      ExampleDao exampleDao) {
    return instance.provideLambdaService(exampleDao);
  }
}

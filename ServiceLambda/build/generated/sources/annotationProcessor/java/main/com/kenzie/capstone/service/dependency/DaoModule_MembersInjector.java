package com.kenzie.capstone.service.dependency;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
public final class DaoModule_MembersInjector implements MembersInjector<DaoModule> {
  private final Provider<DynamoDBMapper> mapperProvider;

  public DaoModule_MembersInjector(Provider<DynamoDBMapper> mapperProvider) {
    this.mapperProvider = mapperProvider;
  }

  public static MembersInjector<DaoModule> create(Provider<DynamoDBMapper> mapperProvider) {
    return new DaoModule_MembersInjector(mapperProvider);
  }

  @Override
  public void injectMembers(DaoModule instance) {
    injectProvideExampleDao(instance, mapperProvider.get());
  }

  public static ExampleDao injectProvideExampleDao(DaoModule instance, DynamoDBMapper mapper) {
    return instance.provideExampleDao(mapper);
  }
}

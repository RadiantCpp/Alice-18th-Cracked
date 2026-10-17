package com.nnpg.alice.bridge;

public interface PayloadRuntimeHost {
   long authorizePayload(Class<?> targetClass);

   default boolean leaseAuthorized() {
      return false;
   }

   default String communityRequest(String request) throws Exception {
      throw new UnsupportedOperationException("Community features disabled");
   }

   default void revokePayload() {
   }
}

package org.utplsql.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * utPLSQL-cli and utPLSQL-maven-plugin rely on this API to bring the classes ojdbc needs
 * for Oracle Wallet (Secure External Password Store) connections.
 * Without them, connecting with "/@TNS_ALIAS" fails with ORA-17167.
 */
class OracleWalletSupportTest {

    @Test
    void oraclePkiIsOnClasspath() {
        assertDoesNotThrow(() -> Class.forName("oracle.security.pki.OracleWallet"));
        assertDoesNotThrow(() -> Class.forName("oracle.security.pki.OracleSecretStore"));
    }
}

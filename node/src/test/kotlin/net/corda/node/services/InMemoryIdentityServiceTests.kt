package net.corda.node.services

import net.corda.core.crypto.Party
<<<<<<< 13e3a72bde9a6450b23fb56d0d5117ee1abc3235
=======
import net.corda.core.crypto.X509Utilities
import net.corda.core.crypto.composite
>>>>>>> Change party to hold an X500 name
import net.corda.core.crypto.generateKeyPair
import net.corda.node.services.identity.InMemoryIdentityService
import net.corda.core.utilities.ALICE
import net.corda.core.utilities.BOB
import net.corda.testing.ALICE_PUBKEY
import net.corda.testing.BOB_PUBKEY
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests for the in memory identity service.
 */
class InMemoryIdentityServiceTests {

    @Test
    fun `get all identities`() {
        val service = InMemoryIdentityService()
        assertNull(service.getAllIdentities().firstOrNull())
        service.registerIdentity(ALICE)
        var expected = setOf(ALICE)
        var actual = service.getAllIdentities().toHashSet()
        assertEquals(expected, actual)

        // Add a second party and check we get both back
        service.registerIdentity(BOB)
        expected = setOf(ALICE, BOB)
        actual = service.getAllIdentities().toHashSet()
        assertEquals(expected, actual)
    }

    @Test
    fun `get identity by key`() {
        val service = InMemoryIdentityService()
        assertNull(service.partyFromKey(ALICE_PUBKEY))
        service.registerIdentity(ALICE)
        assertEquals(ALICE, service.partyFromKey(ALICE_PUBKEY))
        assertNull(service.partyFromKey(BOB_PUBKEY))
    }

    @Test
    fun `get identity by name with no registered identities`() {
        val service = InMemoryIdentityService()
        assertNull(service.partyFromX500Name(ALICE.name))
    }

    @Test
    fun `get identity by name`() {
        val service = InMemoryIdentityService()
        val identities = listOf("Node A", "Node B", "Node C").map { Party(X509Utilities.getDevX509Name(it), generateKeyPair().public) }
        assertNull(service.partyFromX500Name(identities.first().name))
        identities.forEach { service.registerIdentity(it) }
        identities.forEach { assertEquals(it, service.partyFromX500Name(it.name)) }
    }
}

package payment_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Wallet;
import payment_project.repository.WalletRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class WalletRepositoryTest {

    @Autowired
    WalletRepository walletRepository;

    @Test
    void shouldWithdrawSuccess() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(100L);

        walletRepository.save(wallet);

        int updated = walletRepository.withdraw(1L, 50L);

        assertEquals(1, updated);

        Wallet updatedWallet = walletRepository.findById(1L).get();
        assertEquals(50L, updatedWallet.getBalance());
    }

    @Test
    void shouldFailWithdrawWhenNotEnoughMoney() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(30L);

        walletRepository.save(wallet);

        int updated = walletRepository.withdraw(1L, 50L);

        assertEquals(0, updated);

        Wallet unchanged = walletRepository.findById(1L).get();
        assertEquals(30L, unchanged.getBalance());
    }

    @Test
    void shouldDepositSuccess() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(100L);

        walletRepository.save(wallet);

        walletRepository.deposit(1L, 50L);

        Wallet updated = walletRepository.findById(1L).get();
        assertEquals(150L, updated.getBalance());
    }
}

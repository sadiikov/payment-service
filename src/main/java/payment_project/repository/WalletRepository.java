package payment_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import payment_project.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Modifying
    @Query("""
                UPDATE Wallet w
                SET w.balance = w.balance - :amount
                WHERE w.userId = :userId
                  AND w.balance >= :amount
            """)
    int withdraw(Long userId, Long amount);

    @Modifying
    @Query("""
                UPDATE Wallet w
                SET w.balance = w.balance + :amount
                WHERE w.userId = :userId
            """)
    int deposit(Long userId, Long amount);
}

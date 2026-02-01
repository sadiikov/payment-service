package payment_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment_project.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}

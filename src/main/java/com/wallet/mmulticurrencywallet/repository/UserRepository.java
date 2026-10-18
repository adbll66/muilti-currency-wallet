package com.wallet.mmulticurrencywallet.repository;
import com.wallet.mmulticurrencywallet.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {


}


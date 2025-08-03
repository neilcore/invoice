package core.hubby.backend.accounts.services;

import org.springframework.stereotype.Service;

import core.hubby.backend.accounts.mapper.AccountMapper;
import core.hubby.backend.accounts.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
}

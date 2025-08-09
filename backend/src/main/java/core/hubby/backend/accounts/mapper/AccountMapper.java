package core.hubby.backend.accounts.mapper;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;

import core.hubby.backend.accounts.controller.dto.AccountByOrganizationResponse;
import core.hubby.backend.accounts.controller.dto.AccountResponse;
import core.hubby.backend.accounts.entities.Accounts;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface AccountMapper {
	@Mapping(target = "accountID", source = "accountId")
	@Mapping(target = "name", source = "accountName")
	AccountResponse toAccountResponse(Accounts account);
	
	Set<AccountResponse> toAccountResponses(Set<Accounts> accounts);
	
	Set<AccountByOrganizationResponse> toAccountByOrganizationResponses(Set<Accounts> accounts);
	
	@Mappings({
		@Mapping(target = "organizationID", source = "organization.id"),
		@Mapping(target = "accountID", source = "accountId"),
		@Mapping(target = "accountType", source = "accountType.name")
	})
	AccountByOrganizationResponse toAccountByOrganizationResponse(Accounts account);
}

package core.hubby.backend.core.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.hubby.backend.core.dto.PhoneDetail;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressHelper {
	private final ObjectMapper objectMapper;
	private static final Set<String> addressLines = new HashSet<>();
	
	static {
		// initialize address lines
		addressLines.add("addressLine1");
		addressLines.add("addressLine2");
		addressLines.add("addressLine3");
		addressLines.add("addressLine4");
		addressLines.add("city");
		addressLines.add("region");
		addressLines.add("postalCode");
		addressLines.add("country");
		addressLines.add("attentionTo");
	}
	 
    /**
     * Transforms a set of organization address data.
     * Each input map representing an address will be transformed into a new map
     * where keys are converted to camelCase and only valid address fields are retained.
     *
     * @param addresses A set of maps, where each map represents an organization's address.
     * Keys within these maps might not be in camelCase.
     * @return A set of transformed maps, with keys in camelCase and only white-listed fields.
     */
    public Set<Map<String, String>> transformOrganizationAddressData(Set<Map<String, String>> addresses) {
        Set<Map<String, String>> finalAddresses = new HashSet<>();

        if (addresses == null || addresses.isEmpty()) {
            return finalAddresses; // Return empty set if input is null or empty
        }

        for (Map<String, String> addressDetail : addresses) {
            // Create a new map for the current transformed address
            Map<String, String> transformedAddress = new HashMap<>();

            for (Map.Entry<String, String> entry : addressDetail.entrySet()) {
                String originalKey = entry.getKey();
                String originalValue = entry.getValue();

                // Convert the key to camelCase, handling common delimiters like space, underscore, hyphen
                String camelCaseKey = CaseUtils.toCamelCase(originalKey, false, ' ', '_', '-');

                // Check if the camelCased key is in our allowed list
                if (addressLines.contains(camelCaseKey)) {
                    // Add the transformed key and its value to the current transformed address map
                    transformedAddress.put(camelCaseKey, originalValue);
                }
            }

            // Only add the transformed map if it contains any valid fields
            if (!transformedAddress.isEmpty()) {
                finalAddresses.add(transformedAddress);
            }
        }
        return finalAddresses;
    }
    
    /**
     * This method will transform a json address string format
     * to Set<Map<String, String>> object type
     * @param address
     * @return
     */
    public Set<Map<String, String>> jsonAddressStringToSetObject(String address) {
    	Set<Map<String, String>> addressDetail = null;
    	
		try {
			addressDetail = objectMapper.readValue(
					address,
					new TypeReference<Set<Map<String, String>>>() {}
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return addressDetail;
    }
	
}

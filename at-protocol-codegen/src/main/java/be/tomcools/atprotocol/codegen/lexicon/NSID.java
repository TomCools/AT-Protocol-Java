package be.tomcools.atprotocol.codegen.lexicon;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/*
   This is a NameSpaced ID (NSID for short)
   The description is available at: https://atproto.com/specs/nsid
*/
public class NSID {
	private final String separator = "\\.";
	private @JsonValue String content;

	public NSID(String content) {
		this.content = content;
	}

	public String getAuthority() {
		List<String> domainParts = Arrays.asList(getDomain().split(separator));
		Collections.reverse(domainParts);
		return StringUtils.join(domainParts, ".");
	}

	public String getDomain() {
		return content.replace("." + getName(), "");
	}

	// Name is the last entry
	public String getName() {
		String[] split = content.split(separator);
		return split[split.length - 1];
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NSID nsid = (NSID) o;
		return Objects.equals(content, nsid.content);
	}

	@Override
	public int hashCode() {
		return Objects.hash(content);
	}

	@Override
	public String toString() {
		return content;
	}
}

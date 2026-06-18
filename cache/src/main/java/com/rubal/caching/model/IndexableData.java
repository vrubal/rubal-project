package com.rubal.caching.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
public class IndexableData<K, V> {

    @Id
    private String id;

    private K key;

    private V value;

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof IndexableData<?, ?>)) {
            return false;
        } else {
            var other = (IndexableData) o;
            Object this$id = this.getId();
            Object other$id = other.getId();

            if (this$id == null) {
                return other$id == null;
            } else return this$id.equals(other$id);
        }
    }

    public int hashCode() {
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        return result;
    }

}

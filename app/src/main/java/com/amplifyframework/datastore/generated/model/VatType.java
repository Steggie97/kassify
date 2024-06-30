package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the VatType type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "VatTypes", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
public final class VatType implements Model {
  public static final VatTypePath rootPath = new VatTypePath("root", false, null);
  public static final QueryField ID = field("VatType", "id");
  public static final QueryField VAT_NO = field("VatType", "vatNo");
  public static final QueryField VAT_TYPE = field("VatType", "vatType");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int") Integer vatNo;
  private final @ModelField(targetType="String") String vatType;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public Integer getVatNo() {
      return vatNo;
  }
  
  public String getVatType() {
      return vatType;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private VatType(String id, Integer vatNo, String vatType) {
    this.id = id;
    this.vatNo = vatNo;
    this.vatType = vatType;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      VatType vatType = (VatType) obj;
      return ObjectsCompat.equals(getId(), vatType.getId()) &&
              ObjectsCompat.equals(getVatNo(), vatType.getVatNo()) &&
              ObjectsCompat.equals(getVatType(), vatType.getVatType()) &&
              ObjectsCompat.equals(getCreatedAt(), vatType.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), vatType.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getVatNo())
      .append(getVatType())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("VatType {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("vatNo=" + String.valueOf(getVatNo()) + ", ")
      .append("vatType=" + String.valueOf(getVatType()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static VatType justId(String id) {
    return new VatType(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      vatNo,
      vatType);
  }
  public interface BuildStep {
    VatType build();
    BuildStep id(String id);
    BuildStep vatNo(Integer vatNo);
    BuildStep vatType(String vatType);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private Integer vatNo;
    private String vatType;
    public Builder() {
      
    }
    
    private Builder(String id, Integer vatNo, String vatType) {
      this.id = id;
      this.vatNo = vatNo;
      this.vatType = vatType;
    }
    
    @Override
     public VatType build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new VatType(
          id,
          vatNo,
          vatType);
    }
    
    @Override
     public BuildStep vatNo(Integer vatNo) {
        this.vatNo = vatNo;
        return this;
    }
    
    @Override
     public BuildStep vatType(String vatType) {
        this.vatType = vatType;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Integer vatNo, String vatType) {
      super(id, vatNo, vatType);
      
    }
    
    @Override
     public CopyOfBuilder vatNo(Integer vatNo) {
      return (CopyOfBuilder) super.vatNo(vatNo);
    }
    
    @Override
     public CopyOfBuilder vatType(String vatType) {
      return (CopyOfBuilder) super.vatType(vatType);
    }
  }
  

  public static class VatTypeIdentifier extends ModelIdentifier<VatType> {
    private static final long serialVersionUID = 1L;
    public VatTypeIdentifier(String id) {
      super(id);
    }
  }
  
}

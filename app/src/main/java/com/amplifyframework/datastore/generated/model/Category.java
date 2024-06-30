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

/** This is an auto generated class representing the Category type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Categories", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
public final class Category implements Model {
  public static final CategoryPath rootPath = new CategoryPath("root", false, null);
  public static final QueryField ID = field("Category", "id");
  public static final QueryField CATEGORY_NO = field("Category", "categoryNo");
  public static final QueryField CATEGORY_NAME = field("Category", "categoryName");
  public static final QueryField CATEGORY_TYPE = field("Category", "categoryType");
  public static final QueryField IS_ACQUISITION = field("Category", "isAcquisition");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int") Integer categoryNo;
  private final @ModelField(targetType="String") String categoryName;
  private final @ModelField(targetType="CategoryCategoryType") CategoryCategoryType categoryType;
  private final @ModelField(targetType="Boolean") Boolean isAcquisition;
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
  
  public Integer getCategoryNo() {
      return categoryNo;
  }
  
  public String getCategoryName() {
      return categoryName;
  }
  
  public CategoryCategoryType getCategoryType() {
      return categoryType;
  }
  
  public Boolean getIsAcquisition() {
      return isAcquisition;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Category(String id, Integer categoryNo, String categoryName, CategoryCategoryType categoryType, Boolean isAcquisition) {
    this.id = id;
    this.categoryNo = categoryNo;
    this.categoryName = categoryName;
    this.categoryType = categoryType;
    this.isAcquisition = isAcquisition;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Category category = (Category) obj;
      return ObjectsCompat.equals(getId(), category.getId()) &&
              ObjectsCompat.equals(getCategoryNo(), category.getCategoryNo()) &&
              ObjectsCompat.equals(getCategoryName(), category.getCategoryName()) &&
              ObjectsCompat.equals(getCategoryType(), category.getCategoryType()) &&
              ObjectsCompat.equals(getIsAcquisition(), category.getIsAcquisition()) &&
              ObjectsCompat.equals(getCreatedAt(), category.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), category.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getCategoryNo())
      .append(getCategoryName())
      .append(getCategoryType())
      .append(getIsAcquisition())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Category {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("categoryNo=" + String.valueOf(getCategoryNo()) + ", ")
      .append("categoryName=" + String.valueOf(getCategoryName()) + ", ")
      .append("categoryType=" + String.valueOf(getCategoryType()) + ", ")
      .append("isAcquisition=" + String.valueOf(getIsAcquisition()) + ", ")
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
  public static Category justId(String id) {
    return new Category(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      categoryNo,
      categoryName,
      categoryType,
      isAcquisition);
  }
  public interface BuildStep {
    Category build();
    BuildStep id(String id);
    BuildStep categoryNo(Integer categoryNo);
    BuildStep categoryName(String categoryName);
    BuildStep categoryType(CategoryCategoryType categoryType);
    BuildStep isAcquisition(Boolean isAcquisition);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private Integer categoryNo;
    private String categoryName;
    private CategoryCategoryType categoryType;
    private Boolean isAcquisition;
    public Builder() {
      
    }
    
    private Builder(String id, Integer categoryNo, String categoryName, CategoryCategoryType categoryType, Boolean isAcquisition) {
      this.id = id;
      this.categoryNo = categoryNo;
      this.categoryName = categoryName;
      this.categoryType = categoryType;
      this.isAcquisition = isAcquisition;
    }
    
    @Override
     public Category build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Category(
          id,
          categoryNo,
          categoryName,
          categoryType,
          isAcquisition);
    }
    
    @Override
     public BuildStep categoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
        return this;
    }
    
    @Override
     public BuildStep categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }
    
    @Override
     public BuildStep categoryType(CategoryCategoryType categoryType) {
        this.categoryType = categoryType;
        return this;
    }
    
    @Override
     public BuildStep isAcquisition(Boolean isAcquisition) {
        this.isAcquisition = isAcquisition;
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
    private CopyOfBuilder(String id, Integer categoryNo, String categoryName, CategoryCategoryType categoryType, Boolean isAcquisition) {
      super(id, categoryNo, categoryName, categoryType, isAcquisition);
      
    }
    
    @Override
     public CopyOfBuilder categoryNo(Integer categoryNo) {
      return (CopyOfBuilder) super.categoryNo(categoryNo);
    }
    
    @Override
     public CopyOfBuilder categoryName(String categoryName) {
      return (CopyOfBuilder) super.categoryName(categoryName);
    }
    
    @Override
     public CopyOfBuilder categoryType(CategoryCategoryType categoryType) {
      return (CopyOfBuilder) super.categoryType(categoryType);
    }
    
    @Override
     public CopyOfBuilder isAcquisition(Boolean isAcquisition) {
      return (CopyOfBuilder) super.isAcquisition(isAcquisition);
    }
  }
  

  public static class CategoryIdentifier extends ModelIdentifier<Category> {
    private static final long serialVersionUID = 1L;
    public CategoryIdentifier(String id) {
      super(id);
    }
  }
  
}

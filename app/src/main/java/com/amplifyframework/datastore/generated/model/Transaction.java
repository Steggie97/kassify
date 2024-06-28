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

/** This is an auto generated class representing the Transaction type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Transactions", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
}, hasLazySupport = true)
public final class Transaction implements Model {
  public static final TransactionPath rootPath = new TransactionPath("root", false, null);
  public static final QueryField ID = field("Transaction", "id");
  public static final QueryField TRANSACTION_NO = field("Transaction", "transactionNo");
  public static final QueryField DATE = field("Transaction", "date");
  public static final QueryField AMOUNT_PREFIX = field("Transaction", "amountPrefix");
  public static final QueryField AMOUNT = field("Transaction", "amount");
  public static final QueryField ACCOUNT_NO = field("Transaction", "accountNo");
  public static final QueryField CATEGORY_NO = field("Transaction", "categoryNo");
  public static final QueryField VAT_NO = field("Transaction", "vatNo");
  public static final QueryField RECEIPT_NO = field("Transaction", "receiptNo");
  public static final QueryField TRANSACTION_TEXT = field("Transaction", "transactionText");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int") Integer transactionNo;
  private final @ModelField(targetType="AWSDate", isRequired = true) Temporal.Date date;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean amountPrefix;
  private final @ModelField(targetType="Float", isRequired = true) Double amount;
  private final @ModelField(targetType="Int") Integer accountNo;
  private final @ModelField(targetType="Int") Integer categoryNo;
  private final @ModelField(targetType="Int") Integer vatNo;
  private final @ModelField(targetType="String") String receiptNo;
  private final @ModelField(targetType="String") String transactionText;
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
  
  public Integer getTransactionNo() {
      return transactionNo;
  }
  
  public Temporal.Date getDate() {
      return date;
  }
  
  public Boolean getAmountPrefix() {
      return amountPrefix;
  }
  
  public Double getAmount() {
      return amount;
  }
  
  public Integer getAccountNo() {
      return accountNo;
  }
  
  public Integer getCategoryNo() {
      return categoryNo;
  }
  
  public Integer getVatNo() {
      return vatNo;
  }
  
  public String getReceiptNo() {
      return receiptNo;
  }
  
  public String getTransactionText() {
      return transactionText;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Transaction(String id, Integer transactionNo, Temporal.Date date, Boolean amountPrefix, Double amount, Integer accountNo, Integer categoryNo, Integer vatNo, String receiptNo, String transactionText) {
    this.id = id;
    this.transactionNo = transactionNo;
    this.date = date;
    this.amountPrefix = amountPrefix;
    this.amount = amount;
    this.accountNo = accountNo;
    this.categoryNo = categoryNo;
    this.vatNo = vatNo;
    this.receiptNo = receiptNo;
    this.transactionText = transactionText;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Transaction transaction = (Transaction) obj;
      return ObjectsCompat.equals(getId(), transaction.getId()) &&
              ObjectsCompat.equals(getTransactionNo(), transaction.getTransactionNo()) &&
              ObjectsCompat.equals(getDate(), transaction.getDate()) &&
              ObjectsCompat.equals(getAmountPrefix(), transaction.getAmountPrefix()) &&
              ObjectsCompat.equals(getAmount(), transaction.getAmount()) &&
              ObjectsCompat.equals(getAccountNo(), transaction.getAccountNo()) &&
              ObjectsCompat.equals(getCategoryNo(), transaction.getCategoryNo()) &&
              ObjectsCompat.equals(getVatNo(), transaction.getVatNo()) &&
              ObjectsCompat.equals(getReceiptNo(), transaction.getReceiptNo()) &&
              ObjectsCompat.equals(getTransactionText(), transaction.getTransactionText()) &&
              ObjectsCompat.equals(getCreatedAt(), transaction.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), transaction.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTransactionNo())
      .append(getDate())
      .append(getAmountPrefix())
      .append(getAmount())
      .append(getAccountNo())
      .append(getCategoryNo())
      .append(getVatNo())
      .append(getReceiptNo())
      .append(getTransactionText())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Transaction {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("transactionNo=" + String.valueOf(getTransactionNo()) + ", ")
      .append("date=" + String.valueOf(getDate()) + ", ")
      .append("amountPrefix=" + String.valueOf(getAmountPrefix()) + ", ")
      .append("amount=" + String.valueOf(getAmount()) + ", ")
      .append("accountNo=" + String.valueOf(getAccountNo()) + ", ")
      .append("categoryNo=" + String.valueOf(getCategoryNo()) + ", ")
      .append("vatNo=" + String.valueOf(getVatNo()) + ", ")
      .append("receiptNo=" + String.valueOf(getReceiptNo()) + ", ")
      .append("transactionText=" + String.valueOf(getTransactionText()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static DateStep builder() {
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
  public static Transaction justId(String id) {
    return new Transaction(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      transactionNo,
      date,
      amountPrefix,
      amount,
      accountNo,
      categoryNo,
      vatNo,
      receiptNo,
      transactionText);
  }
  public interface DateStep {
    AmountPrefixStep date(Temporal.Date date);
  }
  

  public interface AmountPrefixStep {
    AmountStep amountPrefix(Boolean amountPrefix);
  }
  

  public interface AmountStep {
    BuildStep amount(Double amount);
  }
  

  public interface BuildStep {
    Transaction build();
    BuildStep id(String id);
    BuildStep transactionNo(Integer transactionNo);
    BuildStep accountNo(Integer accountNo);
    BuildStep categoryNo(Integer categoryNo);
    BuildStep vatNo(Integer vatNo);
    BuildStep receiptNo(String receiptNo);
    BuildStep transactionText(String transactionText);
  }
  

  public static class Builder implements DateStep, AmountPrefixStep, AmountStep, BuildStep {
    private String id;
    private Temporal.Date date;
    private Boolean amountPrefix;
    private Double amount;
    private Integer transactionNo;
    private Integer accountNo;
    private Integer categoryNo;
    private Integer vatNo;
    private String receiptNo;
    private String transactionText;
    public Builder() {
      
    }
    
    private Builder(String id, Integer transactionNo, Temporal.Date date, Boolean amountPrefix, Double amount, Integer accountNo, Integer categoryNo, Integer vatNo, String receiptNo, String transactionText) {
      this.id = id;
      this.transactionNo = transactionNo;
      this.date = date;
      this.amountPrefix = amountPrefix;
      this.amount = amount;
      this.accountNo = accountNo;
      this.categoryNo = categoryNo;
      this.vatNo = vatNo;
      this.receiptNo = receiptNo;
      this.transactionText = transactionText;
    }
    
    @Override
     public Transaction build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Transaction(
          id,
          transactionNo,
          date,
          amountPrefix,
          amount,
          accountNo,
          categoryNo,
          vatNo,
          receiptNo,
          transactionText);
    }
    
    @Override
     public AmountPrefixStep date(Temporal.Date date) {
        Objects.requireNonNull(date);
        this.date = date;
        return this;
    }
    
    @Override
     public AmountStep amountPrefix(Boolean amountPrefix) {
        Objects.requireNonNull(amountPrefix);
        this.amountPrefix = amountPrefix;
        return this;
    }
    
    @Override
     public BuildStep amount(Double amount) {
        Objects.requireNonNull(amount);
        this.amount = amount;
        return this;
    }
    
    @Override
     public BuildStep transactionNo(Integer transactionNo) {
        this.transactionNo = transactionNo;
        return this;
    }
    
    @Override
     public BuildStep accountNo(Integer accountNo) {
        this.accountNo = accountNo;
        return this;
    }
    
    @Override
     public BuildStep categoryNo(Integer categoryNo) {
        this.categoryNo = categoryNo;
        return this;
    }
    
    @Override
     public BuildStep vatNo(Integer vatNo) {
        this.vatNo = vatNo;
        return this;
    }
    
    @Override
     public BuildStep receiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
        return this;
    }
    
    @Override
     public BuildStep transactionText(String transactionText) {
        this.transactionText = transactionText;
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
    private CopyOfBuilder(String id, Integer transactionNo, Temporal.Date date, Boolean amountPrefix, Double amount, Integer accountNo, Integer categoryNo, Integer vatNo, String receiptNo, String transactionText) {
      super(id, transactionNo, date, amountPrefix, amount, accountNo, categoryNo, vatNo, receiptNo, transactionText);
      Objects.requireNonNull(date);
      Objects.requireNonNull(amountPrefix);
      Objects.requireNonNull(amount);
    }
    
    @Override
     public CopyOfBuilder date(Temporal.Date date) {
      return (CopyOfBuilder) super.date(date);
    }
    
    @Override
     public CopyOfBuilder amountPrefix(Boolean amountPrefix) {
      return (CopyOfBuilder) super.amountPrefix(amountPrefix);
    }
    
    @Override
     public CopyOfBuilder amount(Double amount) {
      return (CopyOfBuilder) super.amount(amount);
    }
    
    @Override
     public CopyOfBuilder transactionNo(Integer transactionNo) {
      return (CopyOfBuilder) super.transactionNo(transactionNo);
    }
    
    @Override
     public CopyOfBuilder accountNo(Integer accountNo) {
      return (CopyOfBuilder) super.accountNo(accountNo);
    }
    
    @Override
     public CopyOfBuilder categoryNo(Integer categoryNo) {
      return (CopyOfBuilder) super.categoryNo(categoryNo);
    }
    
    @Override
     public CopyOfBuilder vatNo(Integer vatNo) {
      return (CopyOfBuilder) super.vatNo(vatNo);
    }
    
    @Override
     public CopyOfBuilder receiptNo(String receiptNo) {
      return (CopyOfBuilder) super.receiptNo(receiptNo);
    }
    
    @Override
     public CopyOfBuilder transactionText(String transactionText) {
      return (CopyOfBuilder) super.transactionText(transactionText);
    }
  }
  

  public static class TransactionIdentifier extends ModelIdentifier<Transaction> {
    private static final long serialVersionUID = 1L;
    public TransactionIdentifier(String id) {
      super(id);
    }
  }
  
}

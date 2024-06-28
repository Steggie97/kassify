import { type ClientSchema, a, defineData } from '@aws-amplify/backend';

const schema = a.schema({
  Transaction: a
    .model({
      // Buchungsnummer
      transactionNo: a.integer(),
      // Beleg-Datum
      date: a.date().required(),
      // Vorzeichen des Betrags: True -> + , False -> -
      amountPrefix: a.boolean().required(),
      // Betrag
      amount: a.float().required(),
      //Kontonummer der Kasse -> SKR04 Standardkontonummer: 1600
      accountNo: a.integer().default(1600),
      // Kontonummer Gegenkonto - 9999 für keine Zuordnung
      categoryNo: a.integer().default(9999),
      // Buchungsschlüssel für USt-Sachverhalte - bei keiner USt kein Wert hinterlegen
      vatNo: a.integer(),
      // Beleg-Nummer
      receiptNo: a.string().default(""),
      // Buchungstext
      transactionText: a.string().default("")
    })
    .authorization((allow) => [allow.owner()]),

    Category: a
      .model({
        // Gegenkontonummer
        categoryNo: a.integer(),
        // Kontoname
        categoryName: a.string(),
        // Kontotyp
        categoryType: a.enum(['Ertragskonto', 'Aufwandskonto', 'Normalkonto'])
      })
      .authorization((allow) => [allow.authenticated().to(['read'])]),

    VatType: a
      .model({
        // Buchungsschlüssel
        vatNo: a.integer(),
        // Bezeichnung des Buchungsschlüssel
        vatType: a.string(),
      })
      .authorization((allow) => [allow.authenticated().to(['read'])])
});

export type Schema = ClientSchema<typeof schema>;

export const data = defineData({
  schema,
  authorizationModes: {
    defaultAuthorizationMode: 'apiKey',
    apiKeyAuthorizationMode: { expiresInDays: 30 }
  },
});
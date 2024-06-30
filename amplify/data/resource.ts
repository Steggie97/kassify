import { type ClientSchema, a, defineData } from '@aws-amplify/backend';

const schema = a.schema({
  Transaction: a
    .model({
      // Buchungsnummer
      transactionNo: a.integer(),
      // Beleg-Datum
      date: a.date().required(),
      // Vorzeichen des Betrags: True -> + , False -> -
      amountPrefix: a.boolean().required().default(true),
      // Betrag
      amount: a.float().required().default(0.00),
      //Kontonummer der Kasse -> SKR04 Standardkontonummer: 1600
      accountNo: a.integer().required().default(1600),
      // Kontonummer Gegenkonto - 9999 für keine Zuordnung
      categoryNo: a.integer().required().default(9999),
      // Buchungsschlüssel für USt-Sachverhalte - bei keiner USt kein Wert hinterlegen
      vatNo: a.integer().default(null),
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
        categoryType: a.enum(['Ertragskonto', 'Aufwandskonto', 'Normalkonto']),
        //Flag zur Unterscheidung von Einzahlungskonten(true) und Auszahlungskonten(false)
        isAcquisition: a.boolean()
        
      })
      .authorization((allow) => [allow.authenticated()]),

    VatType: a
      .model({
        // Buchungsschlüssel
        vatNo: a.integer(),
        // Bezeichnung des Buchungsschlüssel
        vatType: a.string(),
      })
      .authorization((allow) => [allow.authenticated()])
});

export type Schema = ClientSchema<typeof schema>;

export const data = defineData({
  schema,
  authorizationModes: {
    defaultAuthorizationMode: 'userPool'
  },
});
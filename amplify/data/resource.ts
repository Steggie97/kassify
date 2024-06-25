import { type ClientSchema, a, defineData } from '@aws-amplify/backend';

/*== STEP 1 ===============================================================
The section below creates a Todo database table with a "content" field. Try
adding a new "isDone" field as a boolean. The authorization rule below
specifies that any unauthenticated user can "create", "read", "update", 
and "delete" any "Todo" records.
=========================================================================*/
const schema = a.schema({
  Transaction: a
    .model({
      transId: a.id(),
      date: a.date().required(),
      amountPrefix: a.boolean().required(),
      amount: a.float().required(),
      categoryId: a.id(),
      category: a.belongsTo('Category', 'categoryId'),
      vatId: a.id(),
      vat: a.belongsTo('VatType', 'vatId'),
      receiptNo: a.string().default(""),
      transText: a.string().default("")
    })
    .authorization((allow) => [allow.owner()]),

    Category: a
      .model({
        categoryId: a.id(),
        categorySkr04: a.integer(),
        categoryName: a.string(),
        categoryType: a.enum(['Ertragskonto', 'Aufwandskonto', 'Normalkonto']),
        transactions: a.hasMany('Transaction', 'categoryId')
      })
      .authorization((allow) => [allow.authenticated().to(['read'])]),

    VatType: a
      .model({
        vatId: a.id(),
        vatNo: a.integer(),
        vatType: a.string(),
        transactions: a.hasMany('Transaction', 'vatId')
      })
      .authorization((allow) => [allow.authenticated().to(['read'])])
});

export type Schema = ClientSchema<typeof schema>;

export const data = defineData({
  schema,
  authorizationModes: {
    defaultAuthorizationMode: 'iam',
  },
});

/*== STEP 2 ===============================================================
Go to your frontend source code. From your client-side code, generate a
Data client to make CRUDL requests to your table. (THIS SNIPPET WILL ONLY
WORK IN THE FRONTEND CODE FILE.)

Using JavaScript or Next.js React Server Components, Middleware, Server 
Actions or Pages Router? Review how to generate Data clients for those use
cases: https://docs.amplify.aws/gen2/build-a-backend/data/connect-to-API/
=========================================================================*/

/*
"use client"
import { generateClient } from "aws-amplify/data";
import type { Schema } from "@/amplify/data/resource";

const client = generateClient<Schema>() // use this Data client for CRUDL requests
*/

/*== STEP 3 ===============================================================
Fetch records from the database and use them in your frontend component.
(THIS SNIPPET WILL ONLY WORK IN THE FRONTEND CODE FILE.)
=========================================================================*/

/* For example, in a React component, you can use this snippet in your
  function's RETURN statement */
// const { data: todos } = await client.models.Todo.list()

// return <ul>{todos.map(todo => <li key={todo.id}>{todo.content}</li>)}</ul>

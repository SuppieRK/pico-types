# Pico Types

> The work on this software project is in no way associated with my employer nor with the role I'm having at my
> employer.
>
> I maintain this project alone and as much or as little as my **spare time** permits using my **personal** equipment.

Thin abstract wrappers over Java data types to reinforce your domain with compile-type checks.

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/summary/overall?id=SuppieRK_pico-types)

## Technical aspects

- Built for Java 17.
- No third-party dependencies.
- 100% unit and mutation test coverage.
- `equals` and `hashCode` out of the box, no need to use Lombok - tested by strict form
  of [EqualsVerifier](https://github.com/jqno/equalsverifier).
- Extensibility - all classes are `abstract` and ready to be implemented:
    - In case when you need your own more specific type - you can simply extend `PicoType` itself.
- `Optional`-like API

## What is the problem?

You have probably seen constructs in the code similar to:

```java
// Somewhere across repositories
UUID place(Collection<UUID> entityIds);

// Somewhere in the API
UUID fetchUser(UUID companyId);

// Also can happen within the same API
UUID placeOrder(UUID userId, UUID merchantId);
```

There is nothing conceptually wrong with these examples, and we have smart IDEs to help us fetch the context - but we
can do this:

```java
UUID userId;
UUID merchantId;

// There is nothing wrong with this language wise - except when it will reach production.
userId = placeOrder(merchantId, userId);
```

Wouldn't it be nicer if previous examples could be rewritten with more support from the language itself?

```java
// Oh, we are actually placing items in the cart!
CartId place(Collection<ItemId> ids);

// We were in fact looking for an employee in an affiliate company!
EmployeeId fetchUser(AffiliateCompanyId companyId);

// We won't be able to make the same mistake - it will simply not compile
OrderId placeOrder(UserId userId, MerchantId merchantId);
```

## How the solution is achieved with this library?

Let's take a look at the working example of the previous problem with `placeOrder`:

```java
import java.util.UUID;

public class Solution {
    public static class UserId extends UuidPicoType {
        public UserId(UUID value) {
            super(value);
        }
    }

    public static class MerchantId extends UuidPicoType {
        public MerchantId(UUID value) {
            super(value);
        }
    }

    public static class OrderId extends UuidPicoType {
        public OrderId(UUID value) {
            super(value);
        }
    }

    public static OrderId placeOrder(UserId userId, MerchantId merchantId) {
        return new OrderId(UUID.randomUUID());
    }

    public static void main(String[] args) {
        var userId = new UserId(UUID.randomUUID());
        var merchantId = new MerchantId(UUID.randomUUID());

        // Now you will get a compile time warning if you will try anything from the problem above
        var orderId = placeOrder(userId, merchantId);
        System.out.println(orderId);
    }
}
```

## What are the benefits?

### Improved productivity

We hold on to a lot of context in our heads - tickets to fix, functionality to implement, system design, etc.

Being able to just read the code and understand what needs to be done is a relief.

### Yet another control to maintain the architecture and improve cross-team communication

The main point of the Domain-Driven Design. When there is no confusion around `com.shiny.CustomerUserId` - there is no "
_Hold on, when we say customer ID - what do we mean by customer?_" type of questions on meetings - less time wasted on
explanations for seemingly obvious things to some and completely unknown to others.

## What are the caveats of this approach?

> **To a man with a hammer, everything looks like a nail.**
>
> _Abraham Maslow, The Psychology of Science, 1966_

### Confusion

> Is `UserId` returned from one service the same as `UserId` consumed (or returned) by another service?

The only solution to this problem is adopting Domain-Driven Design and building
a [ubiquitous language dictionary](https://ddd-practitioners.com/home/glossary/ubiquitous-language/).

For example, if we distinguish customers and merchants - it makes sense to stick to the `CustomerUserId` and
`MerchantUserId` (duh!).

### More confusion, this time from Java

> Two services from different teams, where one has `com.shiny.team1.CustomerUserId` and another has
`com.shiny.team2.CustomerUserId` - are these equal?

You probably won't be surprised by the answer - it
is [ubiquitous language dictionary](https://ddd-practitioners.com/home/glossary/ubiquitous-language/) again, this time
in a different flavor:

Depending on the architecture of your system, if all services are consumers of the same API it will make sense to
enforce using specific object from that service, e.g. if everyone connect to `retail` team service, there must be only
`com.shiny.team1.CustomerUserId`.

- However, once this system constraint will be broken (and it will be) there will be a lot of migration pain.

It would be best to introduce a company-wide library with `com.shiny.CustomerUserId`.

It might be tempting to re-wrap IDs to avoid having this library - this will only hide the symptoms and will not solve
the problem.

### Verbosity

> Also known as `ThisTimeThisIsForSureCustomerUserIdPinkySwearAndPromise`.

Please, don't do that. ChatGPT can be quite helpful if you are stuck with the naming.

### Excessiveness

Consider this example:

```java
UUID doingSuperImportantWork(
        UUID userId, 
        UUID companyId, 
        Instant startingFrom, 
        int employeeCount,
        double fare, 
        boolean includeWeekend
);
```

It might be tempting to do something like:

```java
SuperImportantWorkId doingSuperImportantWork(
        UserId userId,
        CompanyId companyId,
        StartTime startingFrom,
        EmployeeCounter employeeCount,
        Fare fare,
        WeekendToggle includeWeekend
);
```

but it is just too verbose - so use this in moderation, like so:

```java
SuperImportantWorkId doingSuperImportantWork(
        UserId userId,
        CompanyId companyId,
        Instant startingFrom,
        int employeeCount,
        Fare fare,
        boolean includeWeekend
);
```

or (better yet) avoid having this many parameters for a method and split it onto smaller methods, otherwise if not
possible introduce a single object, capturing these parameters within.

## Implementation notes

### Why there are no types for `Instant`, etc.?

I rarely saw confusion cases related to dates - we usually tend to either work with `createdDate` and `updatedDate` (
sometimes can be more). If you need to cover this (or any other) specific use case - you can easily create your own
`PicoType`.

### Nullability

In order to make sure these types play nicely with databases, they should support `null`.

With that being said - it is often quite handy to have `Optional`-like API around, if you prefer to maintain null-safety
across your codebase and do not have to write constructs like `Optional<MyPicoType>` which can quickly turn into a
wrapper fest.

### Extending generic signature

Sometimes we need to return more than one value - this is where people turn their attention to classes like `Pair` from
Apache Commons.

While being handy, I suggest to avoid this approach and use proper POJO objects / records.

Another reason why you want to avoid having more than one generic is `Optional`-like API - while it could work under
assumption that both values of both types must not be `null`, we might need to have a combination where one value is
nullable and the other is not: in that case, as I described before, please, consider using POJO / records.

### How these types work with Jackson (GSON, etc.)?

Since you will have to `extend` these classes, you can easily add support for your serialization library - here is an
example for `UUID` and Jackson:

```java
public class MyUuidType extends UuidPicoType {
    public MyUuidType(UUID value) {
        super(value);
    }

    @JsonCreator
    public MyUuidType(String value) {
        this(UUID.fromString(value));
    }

    @Override
    @JsonValue
    public UUID value() {
        return super.value();
    }
}
```

> It is a good idea to make this class `final` and its constructor `private` to disrupt possible inheritance chain.

> This example is taken from `UuidPicoTypeTest` in this repository.

## Inspired by

- [Tiny Types](https://github.com/caligin/tinytypes)
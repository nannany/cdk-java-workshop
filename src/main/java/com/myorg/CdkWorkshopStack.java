package com.myorg;

import com.github.eladb.dynamotableviewer.TableViewer;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

public class CdkWorkshopStack extends Stack {
    public CdkWorkshopStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkWorkshopStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        final Function hello = Function.Builder.create(this, "HelloHandler")
                .runtime(Runtime.NODEJS_10_X)
                .code(Code.fromAsset("lambda"))
                .handler("hello.handler")
                .build();

        final HitCounter helloWithCounter = new HitCounter(this, "HelloHitCounter", HitCounterProps.builder()
                .downstream(hello)
                .build());

        LambdaRestApi.Builder.create(this, "Endpoint")
                .handler(helloWithCounter.getHandler())
                .build();

        // Defines a viewer for the HitCounts table
        TableViewer.Builder.create(this, "ViewerHitCount")
                .title("Hello Hits")
                .table(helloWithCounter.getTable())
                .build();

    }
}

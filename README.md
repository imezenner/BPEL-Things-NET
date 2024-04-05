BPEL Things-Net
================
Java application that allows you to check temporal attributes of a BPEL process using BPEL Things-NET. 

Try the jar: BPELThings-NET-1.0.0-SNAPSHOT

Description
-----------
BPELThings-NET is an open-source Java application that allows you to check the validity of a BPEL process that orchestrates smart things using Restful Web services according to a synchronized scenario. 

Here are the main functionalities provided so far:
* Read input BPEL file;
* Generate BPEL Things-Net structure;
* Fill in missing temporal information;
* Abstract the model to a flattened structure;
* Calculate transition firing dates;
* Run temporal verification and return the result.

Context
-----------
The solution provides a jar executable that allows you to introduce BPEL files, apply the BPEL Things-Net model, and return the final results.

![1](https://github.com/imezenner/BPEL-Things-NET/assets/166130339/d179e4d2-c4ec-44e1-90b1-ab4488f178d2)

If the file is temporally valid, the application returns a success message.

![2](https://github.com/imezenner/BPEL-Things-NET/assets/166130339/ec141f4f-a94e-4f28-97eb-155169f92f28)

If the file is temporally invalid, the application returns an error message.

![3](https://github.com/imezenner/BPEL-Things-NET/assets/166130339/5dfce6a9-6d6a-4e39-8d42-17d60385ef7c)

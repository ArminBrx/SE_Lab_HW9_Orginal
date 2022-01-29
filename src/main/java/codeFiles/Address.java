package codeFiles;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
    public int num;
    public TypeAddress Type;
    public varType varType;

    public Address(int num,varType varType, TypeAddress Type) {
        this.num = num;
        this.Type = Type;
        this.varType = varType;
    }

    public Address(int num,varType varType) {
        this.num = num;
        this.Type = TypeAddress.Direct;
        this.varType = varType;
    }
    public String toString(){
        if(Type == TypeAddress.Direct)
            return num+"";
        if(Type == TypeAddress.Indirect)
            return "@"+num;
        if(Type == TypeAddress.Imidiate)
            return "#"+num;
        return num+"";
    }
}

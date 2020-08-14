package de.ck.jexxatutorial.bookstorej.domain.valueobject;


import io.jexxa.addend.applicationcore.ValueObject;

@ValueObject
public class ISBN13
{
    private String value;

    public ISBN13(final String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }


}
